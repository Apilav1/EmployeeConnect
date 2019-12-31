package com.employeeconnect.data.Server

import android.net.Uri
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSource
import com.employeeconnect.ui.Activities.HomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.employeeconnect.data.Server.User as ServerUser

class FirebaseServer(private val dataMapper: FirebaseDataMapper = FirebaseDataMapper()) : DataSource {

    companion object{

        private val TAG = "CHATTT"
    }

    override fun registerNewUser(
        user: User,
        password: String,
        onSuccess: () -> Unit
    ){
        try {
            FirebaseRegisterUserRequest().execute(user, password, onSuccess)
        }
        catch (e : Exception){
            throw(e)
        }
     }

    override fun getUsers(callback: (ArrayList<User>) -> Unit){

        try{

            FirebaseGetUsersRequest().execute(callback)
        }
        catch (e: Exception){
            throw (e)
        }

    }

    override fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    override fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(uid!!)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val map = snapshot.data?.get("chatRooms") as HashMap<String, String>

//                for(m in map)
//                    Log.d("hhh", m)
                for ((key, value) in map) {
                    Log.d("hhh", "$key = $value")
                }
                Log.d("hhh", snapshot.data?.get("chatRooms")?.toString())
                HomeActivity.currentUser = snapshot.toObject(User::class.java)
            }
        }
    }

    override fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {
        Log.d("CHATTT", "KREIRAM SOBU")

        FirebaseFirestore.getInstance().collection("chatRooms")
            .add(chatRoom)
            .addOnSuccessListener {
                Log.d("CHATTT", "New chat room is created")
                callback(it.id)
            }
            .addOnFailureListener {
                Log.d("CHATTT", "New chat room not created")
            }

    }

    override fun sendMessage(chatRoomId: String, message: Message){

        FirebaseFirestore.getInstance().collection("chatRooms")
            .document(chatRoomId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d(TAG, "message sent")
                updateLatestMessage(chatRoomId, message)
            }
            .addOnFailureListener {
                Log.d(TAG, "message was not sent")
            }
    }

    fun updateLatestMessage(chatRoomId: String, message: Message){
        val docRef = FirebaseFirestore.getInstance().collection("latestMessages")
            .document(chatRoomId)

            docRef.get().addOnSuccessListener { document ->
                if(!document.exists() || document == null){
                    docRef.set(message)
                        .addOnSuccessListener {
                            Log.d(TAG, "new document added to latest messages")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, it.message)
                        }
                }
                else {
                    docRef
                        .update("text", message.text, "fromUser", message.fromUser,
                            "toUser", message.toUser, "timeStamp", message.timeStamp)
                        .addOnSuccessListener {
                            Log.d(TAG, "latest messages updated")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "latest message was not sent ${it.message}")
                        }
                }
            }
    }

    override fun getLatestMessages(callback: (ArrayList<Message>) -> Unit) {
            FirebaseFirestore.getInstance().collection("latestMessages")
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "getting latest messages failed", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val result: ArrayList<Message>

                    if(snapshot != null){
                        result = ArrayList()
                        for(message in snapshot){
                            result.add(message.toObject(Message::class.java))
                        }
                        callback(result)
                    }
                }
    }

    override fun getUserById(userId: String, callback: (User) -> Unit){
        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(userId)

        lateinit var user: User

        docRef.addSnapshotListener { value, e ->

            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(value != null){
                user = value.toObject(User::class.java)!!
                callback(user)
            }
        }
    }

    override fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<User>) -> Unit){
        val ref = FirebaseFirestore.getInstance().collection("users")

        var users: ArrayList<com.employeeconnect.data.Server.User> = ArrayList()

        ref.whereIn("uid", usersIds)
            .get()
            .addOnSuccessListener { value ->
                for (doc in value!!) {
                    users.add(doc.toObject(ServerUser::class.java))
                }
                callback(FirebaseDataMapper().convertToDomain(users))
            }
            .addOnFailureListener {
                Log.d(TAG, "get multiple users failed")
            }
    }

    override fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit){
            FirebaseFirestore.getInstance().collection("chatRooms")
                .document(chatRoomId).collection("messages")
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException != null) {
                            Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                            return@addSnapshotListener
                        }

                        val result: ArrayList<Message>

                        if(snapshot != null){
                            result = ArrayList()
                            for(message in snapshot){
                                result.add(message.toObject(Message::class.java))
                                callback(result)
                            }
                        }
                }
    }

    override fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String) {

        val ref = FirebaseFirestore.getInstance().collection("users")

        val user1 = users[0]
        val user2 = users[1]

        val map1 = user1.chatRooms
        val map2 = user2.chatRooms

        ref.document(user1.uid).update("chatRooms", map1)
            .addOnSuccessListener {
                Log.d("CHATTT", "dodao ")
            }
        ref.document(user2.uid).update("chatRooms", map2)
            .addOnSuccessListener {
                Log.d("CHATTT", "dodao ")
            }

    }

    override fun updateUser(user: User, pictureChaged: Boolean, callback: ()->Unit){

        val docRef = FirebaseFirestore.getInstance().collection("users").document(user.uid)

        docRef.update("currentProject", user.currentProject,
                                    "position", user.position,
                                    "profileImageUrl", user.profileImageUrl,
                                    "skills", user.skills,
                                    "teamName", user.teamName,
                                    "username", user.username)
            .addOnSuccessListener {
                Log.d(TAG, "user updated")

                if(pictureChaged)
                    uploadImageToFirebaseStorage(Uri.parse(user.profileImageUrl))

                callback()
            }
            .addOnFailureListener {
                Log.d(TAG, "user update failed")
            }

    }


    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri){

        val filename = UUID.randomUUID().toString() //random string
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")
                }
            }
            .addOnFailureListener{
                throw(it)
            }

    }


    fun firebaseMessaging(){

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
               // val token = task.result?.token

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
            })
    }

}
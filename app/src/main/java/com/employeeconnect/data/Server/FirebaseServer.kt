package com.employeeconnect.data.Server

import android.content.Intent
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

    override fun getCurrentUserId(callback: (uid: String?) -> Unit){
        var uid: String?

        uid = FirebaseAuth.getInstance().uid
        Log.d("CHATTT", "uid: $uid")
        callback(uid)
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
                val map = snapshot.data?.get("chatRooms") as HashMap<*, *>

//                for(m in map)
//                    Log.d("hhh", m)
                Log.d("CHATTT", "-------------------currentuser-------")

                for ((key, value) in map) {
                    Log.d("CHATTT", "$key = $value")
                }
                Log.d("CHATTT", snapshot.data?.get("chatRooms")?.toString())
                HomeActivity.currentUser = snapshot.toObject(User::class.java)
                HomeActivity.currentUserId = HomeActivity.currentUser!!.uid
                Log.d("CHATTT", "---------------${HomeActivity.currentUser}-----------------------")
            }
        }
    }

    override fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {
        Log.d("CHATTT", "KREIRAM SOBU")

        FirebaseFirestore.getInstance().collection("chatRooms")
            .add(chatRoom)
            .addOnSuccessListener {
                Log.d("CHATTT", "New chat room is created")
                updateChatRoomWithId(it.id)
                callback(it.id)
            }
            .addOnFailureListener {
                Log.d("CHATTT", "New chat room not created")
            }

    }

    fun updateChatRoomWithId(chatRoomId: String){
        FirebaseFirestore.getInstance().collection("chatRooms").document(chatRoomId)
            .update("uid", chatRoomId)
            .addOnSuccessListener {
                Log.d(TAG, "Chatroom updated successful")
            }
            .addOnFailureListener {
                Log.d(TAG, it.message)
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

    override fun getLatestMessages(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit) {
            val ref = FirebaseFirestore.getInstance().collection("latestMessages")
                ref.whereIn("chatRoomId", chatRooms)
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
                        Log.d(TAG, "GET LATEST MESSAGES: ${result.size.toString()}")
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
                    val userr = doc.toObject(ServerUser::class.java)
                    users.add(userr)
                    Log.d("CHATTT", "vracam->"+userr.username)
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

    override fun updateUser(user: User, pictureChanged: Boolean, callback: ()->Unit){

        val docRef = FirebaseFirestore.getInstance().collection("users").document(user.uid)

        docRef.update("currentProject", user.currentProject,
                                    "position", user.position,
                                    "profileImageUrl", user.profileImageUrl,
                                    "skills", user.skills,
                                    "teamName", user.teamName,
                                    "username", user.username)
            .addOnSuccessListener {
                Log.d(TAG, "user updated")

                if(pictureChanged)
                    uploadImageToFirebaseStorage(Uri.parse(user.profileImageUrl))

                callback()
            }
            .addOnFailureListener {
                Log.d(TAG, "user update failed")
            }

    }

    override fun deleteUser(userId: String, callback: () -> Unit){

        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "User successfully deleted")
                callback()
            }
            .addOnFailureListener {
                Log.d(TAG, "User delete problem")
            }

        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
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

    override fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (signInSuccessful: Boolean) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                callback(it.isSuccessful)
            }
            .addOnFailureListener {
                Log.d("CHATTT", "FAILED ${it.message}")

                }
    }

    override fun logoutUser(callback: () -> Unit){
        FirebaseAuth.getInstance().signOut()
        callback()
    }

    override fun verifyUserProfile(userId: String, callback: () -> Unit){

        val ref = FirebaseFirestore.getInstance().collection("users").document(userId)

        ref.update("verified", true)
            .addOnSuccessListener {
                callback()
            }
            .addOnFailureListener {
                Log.d(TAG, "verification failed")
            }
    }

    override fun makeUserAModerator(userId: String, callback: () -> Unit){

        val ref = FirebaseFirestore.getInstance().collection("users").document(userId)

        ref.update("moderator", true)
            .addOnSuccessListener {
                callback()
            }
            .addOnFailureListener {
                Log.d(TAG, "verification failed")
            }
    }

    override fun checkIfUserIsVerified(email: String, callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit){
        val ref = FirebaseFirestore.getInstance().collection("users")

        ref.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.size()>0) {
                    val user = snapshot.documents[0].toObject(ServerUser::class.java)
                    callback(true, user!!.verified)
                }
                else{
                    callback(false, false)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "verification check failed")
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
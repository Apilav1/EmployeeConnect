package com.employeeconnect.data.Server

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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

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
            }
            .addOnFailureListener {
                Log.d(TAG, "message was not sent")
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
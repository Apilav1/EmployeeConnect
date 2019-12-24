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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class FirebaseServer(private val dataMapper: FirebaseDataMapper = FirebaseDataMapper()) : DataSource {

    companion object{

        private val TAG = "FirebaseSERVER"
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

        FirebaseFirestore.getInstance().collection("chatRooms").document(chatRoomId).collection("messages")
            .add(message)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d(TAG, "New chat room messages not created")
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
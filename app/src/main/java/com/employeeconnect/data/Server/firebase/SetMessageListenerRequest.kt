package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class SetMessageListenerRequest {

    fun execute(chatRoomId: String, callback: (ArrayList<Message>) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        FirebaseFirestore.getInstance().collection("chatRooms")
            .document(chatRoomId).collection("messages")
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(FirebaseServer.TAG, "Listen failed.", firebaseFirestoreException)
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
}
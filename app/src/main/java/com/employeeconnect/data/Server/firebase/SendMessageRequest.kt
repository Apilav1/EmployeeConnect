package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class SendMessageRequest {

    fun execute(chatRoomId: String, message: Message, callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return


        FirebaseFirestore.getInstance().collection("chatRooms")
            .document(chatRoomId).collection("messages")
            .add(FirebaseDataMapper().convertMessageToServer(message))
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "message sent")
                UpdateLatestMessageRequest().execute(chatRoomId, message){}

                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "message was not sent")
            }
    }
}
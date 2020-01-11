package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateLatestMessageRequest {

    fun execute(chatRoomId: String, message: Message){

        if(!BaseActivity.deviceIsConnected) return

        val docRef = FirebaseFirestore.getInstance().collection("latestMessages")
            .document(chatRoomId)

        docRef.get().addOnSuccessListener { document ->
            if(!document.exists() || document == null){
                docRef.set(message)
                    .addOnSuccessListener {
                        Log.d(FirebaseServer.TAG, "new document added to latest messages")
                    }
                    .addOnFailureListener {
                        Log.d(FirebaseServer.TAG, it.message)
                    }
            }
            else {
                docRef
                    .update("text", message.text, "fromUser", message.fromUser,
                        "toUser", message.toUser, "timeStamp", message.timeStamp)
                    .addOnSuccessListener {
                        Log.d(FirebaseServer.TAG, "latest messages updated")
                    }
                    .addOnFailureListener {
                        Log.d(FirebaseServer.TAG, "latest message was not sent ${it.message}")
                    }
            }
        }
    }
}
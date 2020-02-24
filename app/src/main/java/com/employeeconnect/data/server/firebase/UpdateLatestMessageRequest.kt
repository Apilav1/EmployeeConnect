package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message as DomainMessage
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateLatestMessageRequest {

    fun execute(chatRoomId: String, message: DomainMessage, callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val docRef = FirebaseFirestore.getInstance().collection("latestMessages")
            .document(chatRoomId)

        docRef.get().addOnSuccessListener { document ->
            if(!document.exists() || document == null){
                docRef.set(FirebaseDataMapper().convertMessageToServer(message))
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
                        "toUser", message.toUser, "timeStamp", message.timeStamp, "seen", message.seen)
                    .addOnSuccessListener {
                        Log.d(FirebaseServer.TAG, "latest messages updated")
                        callback()
                    }
                    .addOnFailureListener {
                        Log.d(FirebaseServer.TAG, "latest message was not sent ${it.message}")
                    }
            }
        }
    }
}
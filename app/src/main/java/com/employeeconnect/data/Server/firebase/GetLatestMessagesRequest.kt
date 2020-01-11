package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class GetLatestMessagesRequest {

    fun execute(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit) {

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("latestMessages")
        ref.whereIn("chatRoomId", chatRooms)
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(FirebaseServer.TAG, "getting latest messages failed", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val result: ArrayList<Message>

                if(snapshot != null){
                    result = ArrayList()
                    for(message in snapshot){
                        result.add(message.toObject(Message::class.java))
                    }
                    Log.d(FirebaseServer.TAG, "GET LATEST MESSAGES: ${result.size.toString()}")
                    callback(result)
                }
            }
    }
}
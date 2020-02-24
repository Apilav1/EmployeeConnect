package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.employeeconnect.data.server.firebase.Message as ServerMessage

class GetLatestMessagesRequest {

    fun execute(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit) {

        if(!BaseActivity.deviceIsConnected || chatRooms.isEmpty()) return

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
                        val res = message.toObject(ServerMessage::class.java)
                        result.add(FirebaseDataMapper().convertMessageToDomain(res))
                    }
                    Log.d(FirebaseServer.TAG, "GET LATEST MESSAGES: ${result.size.toString()}")
                    callback(result)
                }
            }
    }
}
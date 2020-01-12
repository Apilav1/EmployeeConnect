package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.employeeconnect.data.Server.firebase.Message as ServerMessage
import com.employeeconnect.domain.Models.Message as DomainMessage

class SetMessageListenerRequest {

    fun execute(chatRoomId: String, callback: (ArrayList<DomainMessage>) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        FirebaseFirestore.getInstance().collection("chatRooms")
            .document(chatRoomId).collection("messages")
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(FirebaseServer.TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val result: ArrayList<DomainMessage>

                if(snapshot != null){
                    result = ArrayList()
                    for(message in snapshot){
                        val res = message.toObject(ServerMessage::class.java)
                        result.add(FirebaseDataMapper().convertMessageToDomain(res))
                        callback(result)
                    }
                }
            }
    }
}
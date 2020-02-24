package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class CreateChatRoomRequest {

    fun execute(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {

        if(!BaseActivity.deviceIsConnected) return

        val serverChatRoom = FirebaseDataMapper().convertChatRoomToServer(chatRoom)

        FirebaseFirestore.getInstance().collection("chatRooms")
            .add(serverChatRoom)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "New chat room is created")
                updateChatRoomWithId(it.id)
                callback(it.id)
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "New chat room not created")
            }

    }

    fun updateChatRoomWithId(chatRoomId: String){
        FirebaseFirestore.getInstance().collection("chatRooms").document(chatRoomId)
            .update("uid", chatRoomId)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "Chatroom updated successful")
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, it.message)
            }
    }
}
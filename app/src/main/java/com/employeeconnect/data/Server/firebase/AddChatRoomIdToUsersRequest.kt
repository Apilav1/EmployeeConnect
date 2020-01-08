package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.Activities.BaseActivity
import com.google.firebase.FirebaseError
import com.google.firebase.firestore.FirebaseFirestore

class AddChatRoomIdToUsersRequest {

    fun execute(users: ArrayList<User>, chatRoomId: String) {

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("users")

        val user1 = users[0]
        val user2 = users[1]

        val map1 = user1.chatRooms
        val map2 = user2.chatRooms

        ref.document(user1.uid).update("chatRooms", map1)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "dodao ")
            }
        ref.document(user2.uid).update("chatRooms", map2)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "dodao ")
            }

    }
}
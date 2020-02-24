package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class MakeUserAModeratorRequest {

    fun execute(userId: String, callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("users").document(userId)

        ref.update("moderator", true)
            .addOnSuccessListener {
                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "verification failed")
            }
    }
}
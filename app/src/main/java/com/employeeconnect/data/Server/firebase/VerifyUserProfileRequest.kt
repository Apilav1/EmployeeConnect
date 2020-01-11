package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class VerifyUserProfileRequest {

    fun execute(userId: String, callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("users").document(userId)

        ref.update("verified", true)
            .addOnSuccessListener {
                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "verification failed")
            }
    }
}
package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUserRequest {

    fun execute(userId: String, callback: () -> Unit) {

        if(!BaseActivity.deviceIsConnected) return

        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "User successfully deleted")
                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "User delete problem")
            }

        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(FirebaseServer.TAG, "User account deleted.")
                }
            }
    }
}
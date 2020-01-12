package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.activities.BaseActivity
import com.employeeconnect.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FetchCurrentUserRequest {

    fun execute(callback: (user: User) -> Unit) {

        if(!BaseActivity.deviceIsConnected) return

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(uid!!)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(FirebaseServer.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val result = snapshot.toObject(User::class.java)
                callback(result!!)
            }
        }
    }
}
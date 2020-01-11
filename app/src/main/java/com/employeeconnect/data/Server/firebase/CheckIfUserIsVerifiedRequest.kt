package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class CheckIfUserIsVerifiedRequest{

    fun execute(email: String, callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("users")

        ref.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.size()>0) {
                    val user = snapshot.documents[0].toObject(User::class.java)
                    callback(true, user!!.verified)
                }
                else{
                    callback(false, false)
                }
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "verification check failed")
            }
    }
}
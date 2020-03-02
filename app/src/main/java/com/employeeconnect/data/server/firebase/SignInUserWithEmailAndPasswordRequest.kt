package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.data.db.EmployeeConnectDb
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class SignInUserWithEmailAndPasswordRequest {

    fun execute(
        email: String,
        password: String,
        db: EmployeeConnectDb = EmployeeConnectDb(),
        callback: (signInSuccessful: Boolean) -> Unit
    ) {

        if(!BaseActivity.deviceIsConnected) return

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                db.saveCurrentUserId(it.result!!.user!!.uid)
                callback(it.isSuccessful)

            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "FAILED ${it.message}")
            }
    }
}
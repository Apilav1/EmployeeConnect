package com.employeeconnect.data.Server

import android.util.Log
import com.employeeconnect.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class MyFirebaseInstanceId{

    private val TAG = "FIREBASEINSTANCEID"

    fun onTokenRefresh() {

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                Log.d(TAG, token)
            })
    }
}
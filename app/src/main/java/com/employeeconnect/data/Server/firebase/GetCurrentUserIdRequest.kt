package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.Activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class GetCurrentUserIdRequest {

    fun execute(callback: (uid: String?) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        callback(FirebaseAuth.getInstance().uid)
    }
}
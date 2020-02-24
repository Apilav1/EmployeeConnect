package com.employeeconnect.data.server.firebase

import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class GetCurrentUserIdRequest {

    fun execute(callback: (uid: String?) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        callback(FirebaseAuth.getInstance().uid)
    }
}
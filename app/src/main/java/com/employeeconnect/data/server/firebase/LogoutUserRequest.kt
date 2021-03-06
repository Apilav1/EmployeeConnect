package com.employeeconnect.data.server.firebase

import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutUserRequest {

    fun execute(callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        FirebaseAuth.getInstance().signOut()
        callback()
    }
}
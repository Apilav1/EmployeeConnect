package com.employeeconnect.data.Server.firebase

import com.employeeconnect.ui.Activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutUserRequest {

    fun execute(callback: () -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        FirebaseAuth.getInstance().signOut()
        callback()
    }
}
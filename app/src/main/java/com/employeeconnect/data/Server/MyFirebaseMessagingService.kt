package com.employeeconnect.data.Server

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        handleNotification(p0.notification.toString())
    }

    private fun handleNotification(body: String){
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message", body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }

    companion object{
        private const val TAG = "MyFirebaseMSService"
    }
}
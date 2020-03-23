package com.employeeconnect.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.employeeconnect.networks.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * "it is good to have a base class for an application because we can write lot of common tasks in this class and all the other activities implementing from this activity class will get the defined functionalities automatically."
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mSnackBar: Snackbar? = null
    private var receiver: ConnectivityReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiver = ConnectivityReceiver()

        registerReceiver(receiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    private fun showMessage(isConnected: Boolean) {

        deviceIsConnected = isConnected

        if (!isConnected) {

            val messageToUser = "You are offline now."
            mSnackBar = Snackbar.make(findViewById(android.R.id.content),
                                        messageToUser, Snackbar.LENGTH_LONG)
            mSnackBar?.duration = BaseTransientBottomBar.LENGTH_LONG
            mSnackBar?.show()

        } else {
            mSnackBar?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val intent = Intent(BROADCAST_NETWORK_CHANGED)
        intent.putExtra(ISCONNECTED, isConnected)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        showMessage(isConnected)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    companion object{
         var deviceIsConnected = false
         const val ISCONNECTED = "isConnected"
         const val BROADCAST_NETWORK_CHANGED = "networkChanged"
    }
}
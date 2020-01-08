package com.employeeconnect.ui.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.employeeconnect.R
import com.employeeconnect.networks.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * "it is good to have a base class for an application because we can write lot of common tasks in this class and all the other activities implementing from this activity class will get the defined functionalities automatically."
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
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
        showMessage(isConnected)
    }

    companion object{
         var deviceIsConnected = true
    }
}
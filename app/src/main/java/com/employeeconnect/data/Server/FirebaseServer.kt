package com.employeeconnect.data.Server

import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class FirebaseServer(private val dataMapper: FirebaseDataMapper = FirebaseDataMapper()) : DataSource {

    private val TAG = "FirebaseSERVER"

    override fun registerNewUser(
        user: User,
        password: String,
        onSuccess: () -> Unit
    ){
        try {
            FirebaseRegisterUserRequest().execute(user, password, onSuccess)
        }
        catch (e : Exception){
            throw(e)
        }
     }

    override fun getUsers(callback: (ArrayList<User>) -> Unit){

        try{

            FirebaseGetUsersRequest().execute(callback)
        }
        catch (e: Exception){
            throw (e)
        }

    }

    fun firebaseMessaging(){

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
            })
    }

}
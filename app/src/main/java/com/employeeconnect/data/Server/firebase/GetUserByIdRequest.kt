package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.Activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class GetUserByIdRequest {

    fun execute(userId: String, callback: (User) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(userId)

        lateinit var user: User

        docRef.addSnapshotListener { value, e ->

            if (e != null) {
                Log.w(FirebaseServer.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(value != null){
                user = value.toObject(User::class.java)!!
                callback(user)
            }
        }
    }
}
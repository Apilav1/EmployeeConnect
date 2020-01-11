package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class GetMultipleUsersByIdRequest {

    fun execute(usersIds: ArrayList<String>, callback: (ArrayList<com.employeeconnect.domain.Models.User>) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance().collection("users")

        var users: ArrayList<User> = ArrayList()

        ref.whereIn("uid", usersIds)
            .get()
            .addOnSuccessListener { value ->
                for (doc in value!!) {
                    val userr = doc.toObject(User::class.java)
                    users.add(userr)
                }
                callback(FirebaseDataMapper().convertToDomain(users))
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "get multiple users failed")
            }
    }
}
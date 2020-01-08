package com.employeeconnect.data.Server.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.Server.firebase.User as ServerUser


class FirebaseGetUsersRequest {

    val TAG = "FirebaseGetUsersRequest"

    fun execute(callback: (ArrayList<DomainUser>) -> Unit){

        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users")

        lateinit var users: ArrayList<ServerUser>

        docRef.addSnapshotListener { value, e ->
            users = ArrayList()

            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (doc in value!!) {
                users.add(doc.toObject(ServerUser::class.java))
            }
            callback(FirebaseDataMapper().convertToDomain(users))
        }

//        docRef.get()
//            .addOnSuccessListener { documents ->
//                for(document in documents){
//                    users.add(document.toObject(ServerUser::class.java))
//                    Log.d("Homeee", "izvorno"+users.size.toString())
//                }
//                callback(FirebaseDataMapper().convertToDomain(users))
//            }
//            .addOnFailureListener {
//                throw (it)
//            }
    }
}
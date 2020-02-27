package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.employeeconnect.data.db.EmployeeConnectDb
import com.google.firebase.firestore.FirebaseFirestore

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.server.firebase.User as ServerUser


class FirebaseGetUsersRequest( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                               private val db: EmployeeConnectDb = EmployeeConnectDb()
) {

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
                val userr = doc.toObject(ServerUser::class.java)
                users.add(userr)
            }

            val converted = FirebaseDataMapper().convertToDomain(users)
            db.saveUsers(converted)
            callback(converted)
        }
    }
}
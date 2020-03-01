package com.employeeconnect.data.server.firebase

import android.util.Log
import com.employeeconnect.data.db.EmployeeConnectDb
import com.google.firebase.firestore.FirebaseFirestore

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.server.firebase.User as ServerUser


class FirebaseGetUsersRequest( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                               private val db: EmployeeConnectDb = EmployeeConnectDb()
) {
    companion object{

        val TAG = "FirebaseGetUsersRequest"

    }

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
                val serverUsers = doc.toObject(ServerUser::class.java)
                users.add(serverUsers)
            }


            GetUsersPicturesInBitmapsRequest(dataMapper, db).execute(users){ callback(it) }

            val converted = FirebaseDataMapper().convertToDomain(users)

            callback(converted)

        }
    }


}
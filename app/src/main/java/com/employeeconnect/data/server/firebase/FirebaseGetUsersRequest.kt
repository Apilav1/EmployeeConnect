package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.employeeconnect.data.db.EmployeeConnectDb
import com.google.firebase.firestore.FirebaseFirestore
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync

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

//            doAsync {
//                val imageLoader = ImageLoader.getInstance()
//                userr.profileImage = imageLoader.loadImageSync(userr.profileImageUrl)
//            }
            val converted = FirebaseDataMapper().convertToDomain(users)
            db.saveUsers(converted)
            callback(converted)
        }
    }
}
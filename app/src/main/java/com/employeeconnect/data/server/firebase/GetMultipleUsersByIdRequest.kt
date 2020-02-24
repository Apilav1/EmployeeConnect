package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener


class GetMultipleUsersByIdRequest {

    fun execute(usersIds: ArrayList<String>, callback: (ArrayList<com.employeeconnect.domain.Models.User>) -> Unit){

        if(!BaseActivity.deviceIsConnected || usersIds.isEmpty()) return

        val ref = FirebaseFirestore.getInstance().collection("users")

        var users: ArrayList<User> = ArrayList()

        ref.whereIn("uid", usersIds)
            .get()
            .addOnSuccessListener { value ->
                for (doc in value!!) {
                    val userr = doc.toObject(User::class.java)
                    users.add(userr)

                    /*val imageLoader = ImageLoader.getInstance()
                    imageLoader.loadImage(userr.profileImageUrl, object : SimpleImageLoadingListener(){
                        override fun onLoadingComplete(
                            imageUri: String?,
                            view: View?,
                            loadedImage: Bitmap?
                        ) {
                            super.onLoadingComplete(imageUri, view, loadedImage)
                            Log.d("CHATTT", "VALJA")
                            userr.profileImage = loadedImage
                        }

                        override fun onLoadingFailed(
                            imageUri: String?,
                            view: View?,
                            failReason: FailReason?
                        ) {
                            super.onLoadingFailed(imageUri, view, failReason)
                            Log.d("CHATTT", "ne valja")
                        }
                    })*/
                }
                callback(FirebaseDataMapper().convertToDomain(users))
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "get multiple users failed")
            }
    }
}
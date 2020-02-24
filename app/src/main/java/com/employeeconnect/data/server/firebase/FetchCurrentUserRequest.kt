package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.employeeconnect.ui.activities.BaseActivity
import com.employeeconnect.data.server.firebase.User as ServerUser
import com.employeeconnect.domain.Models.User as DomainUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener

class FetchCurrentUserRequest {

    fun execute(callback: (user: DomainUser) -> Unit) {

        if(!BaseActivity.deviceIsConnected) return

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(uid!!)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(FirebaseServer.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val result= snapshot.toObject(ServerUser::class.java)

                val imageLoader = ImageLoader.getInstance()

                /*imageLoader.loadImage(result?.profileImageUrl, object : SimpleImageLoadingListener(){
                    override fun onLoadingComplete(
                        imageUri: String?,
                        view: View?,
                        loadedImage: Bitmap?
                    ) {
                        super.onLoadingComplete(imageUri, view, loadedImage)
                        Log.d("CHATTT", "VALJA")
                        result?.profileImage = loadedImage
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
                callback(FirebaseDataMapper().convertUserToDomain(result!!))
            }
        }
    }
}
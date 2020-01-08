package com.employeeconnect.data.Server.firebase

import android.net.Uri
import android.util.Log
import com.employeeconnect.ui.Activities.BaseActivity
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadImageToFirebaseStorageRequest {

    fun execute(selectedPhotoUri: Uri) {

        if(!BaseActivity.deviceIsConnected) return

        val filename = UUID.randomUUID().toString() //random string
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(FirebaseServer.TAG, "File Location: $it")
                }
            }
            .addOnFailureListener {
                throw(it)
            }
    }
}
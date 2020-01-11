package com.employeeconnect.data.Server.firebase

import android.net.Uri
import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUserRequest {

    fun execute(user: User, pictureChanged: Boolean, callback: ()->Unit){

        if(!BaseActivity.deviceIsConnected) return

        val docRef = FirebaseFirestore.getInstance().collection("users").document(user.uid)

        docRef.update("currentProject", user.currentProject,
            "position", user.position,
            "profileImageUrl", user.profileImageUrl,
            "skills", user.skills,
            "teamName", user.teamName,
            "username", user.username)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "user updated")

                if(pictureChanged)
                    UploadImageToFirebaseStorageRequest().execute(Uri.parse(user.profileImageUrl))
                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "user update failed")
            }

    }
}
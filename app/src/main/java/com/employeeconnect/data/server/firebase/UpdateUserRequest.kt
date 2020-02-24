package com.employeeconnect.data.server.firebase

import android.net.Uri
import android.util.Log
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUserRequest {

    fun execute(user: DomainUser, pictureChanged: Boolean, callback: ()->Unit){

        if(!BaseActivity.deviceIsConnected) return

        val docRef = FirebaseFirestore.getInstance().collection("users").document(user.uid)

        val userServer = FirebaseDataMapper().convertUserToServer(user)

        docRef.update("currentProject", userServer.currentProject,
            "position", userServer.position,
            "profileImageUrl", userServer.profileImageUrl,
            "skills", userServer.skills,
            "teamName", userServer.teamName,
            "username", userServer.username)
            .addOnSuccessListener {
                Log.d(FirebaseServer.TAG, "user updated")

                if(pictureChanged)
                    UploadImageToFirebaseStorageRequest().execute(Uri.parse(userServer.profileImageUrl))
                callback()
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "user update failed")
            }

    }
}
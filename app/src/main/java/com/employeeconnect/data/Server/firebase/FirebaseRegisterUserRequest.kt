package com.employeeconnect.data.Server.firebase

import android.net.Uri
import android.util.Log
import com.employeeconnect.domain.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseRegisterUserRequest {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private lateinit var onSuccess: () -> Unit

    private val TAG = "FirebaseHelper"

    fun execute(
        user: User,
        password: String,
        onSuccess: () -> Unit
    ){

        auth = FirebaseAuth.getInstance()
        this.user = user
        this.onSuccess = onSuccess

        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {

                user.uid = auth.uid ?: ""
                uploadImageToFirebaseStorage(Uri.parse(user.profileImageUrl))
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                throw(it)
            }

    }

     private fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri){

        val filename = UUID.randomUUID().toString() //random string
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    user.profileImageUrl = it.toString()
                    saveUserToFirebaseDatabase()
                }
            }
            .addOnFailureListener{
                throw(it)
            }

    }

    private fun saveUserToFirebaseDatabase(){

        val ref = FirebaseFirestore.getInstance()

        val newUser = ref.collection("users").document(user.uid)

        newUser.set(user)
            .addOnSuccessListener {
                Log.d(TAG, "Document added")
                onSuccess()
            }
            .addOnFailureListener {
                throw(it)
            }

    }
}
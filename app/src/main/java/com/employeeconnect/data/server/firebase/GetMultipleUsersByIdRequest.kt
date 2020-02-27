package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.ui.App
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.net.URL

class GetMultipleUsersByIdRequest {

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            coroutineScope.launch {
                val errorMessage = App.instance?.getString(R.string.error_loading_message)
                showToast(errorMessage)
            }

        }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

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
                }

                val converted = FirebaseDataMapper().convertToDomain(users)

                converted.forEach {
                    coroutineScope.launch(Dispatchers.Main) {

                        it.profileImage = getBitmapOfURL(it.profileImageUrl)

                        if(checkUserProfilePics(converted))
                            GlobalScope.launch(Dispatchers.Main) {
                                callback(converted)
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.d(FirebaseServer.TAG, "get multiple users failed")
            }
    }

    private fun checkUserProfilePics(converted: ArrayList<com.employeeconnect.domain.Models.User>): Boolean{
        //checking if all bitmaps are set
        converted.forEach {
            if(it.profileImage == null) return false
        }
        return true
    }

    private suspend fun getBitmapOfURL(url: String): Bitmap =
        withContext(Dispatchers.IO){
            URL(url).openStream().use {
                return@withContext BitmapFactory.decodeStream(it)
            }
        }

    private fun showToast(error: String?){
        val context = App.instance
        GlobalScope.launch(Dispatchers.Main){
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}
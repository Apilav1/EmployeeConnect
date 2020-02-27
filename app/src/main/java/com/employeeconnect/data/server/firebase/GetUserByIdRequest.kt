package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.server.firebase.User as ServerUser
import com.employeeconnect.ui.App
import com.employeeconnect.ui.activities.BaseActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.net.URL

class GetUserByIdRequest {

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            coroutineScope.launch {
                val errorMessage = App.instance?.getString(R.string.error_loading_message)
                showToast(errorMessage)
            }

        }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

    fun execute(userId: String, callback: (DomainUser) -> Unit){

        if(!BaseActivity.deviceIsConnected) return

        val ref = FirebaseFirestore.getInstance()
        val docRef = ref.collection("users").document(userId)

        docRef.addSnapshotListener { value, e ->

            if (e != null) {
                Log.w(FirebaseServer.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(value != null){

                val result = value.toObject(ServerUser::class.java)

                coroutineScope.launch(Dispatchers.Main){

                    result!!.profileImage = getBitmapOfURL(result.profileImageUrl)

                    callback(FirebaseDataMapper().convertUserToDomain(result))
                }
            }
        }
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
package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.ui.App
import com.employeeconnect.ui.activities.BaseActivity
import com.employeeconnect.data.server.firebase.User as ServerUser
import com.employeeconnect.domain.Models.User as DomainUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.net.URL

class FetchCurrentUserRequest {

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            coroutineScope.launch {
                val errorMessage = App.instance?.getString(R.string.error_loading_message)
                showToast(errorMessage)
            }

        }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

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

                val result = snapshot.toObject(ServerUser::class.java)

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
        GlobalScope.launch(Main){
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}
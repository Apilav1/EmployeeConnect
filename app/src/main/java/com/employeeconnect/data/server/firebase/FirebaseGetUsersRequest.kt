package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.data.db.EmployeeConnectDb
import com.employeeconnect.ui.App
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.net.URL

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.server.firebase.User as ServerUser


class FirebaseGetUsersRequest( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                               private val db: EmployeeConnectDb = EmployeeConnectDb()
) {
    companion object{

        val TAG = "FirebaseGetUsersRequest"

    }

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            coroutinScope.launch {
                val errorMessage = App.instance?.getString(R.string.error_loading_message)
                showToast(errorMessage)
            }

        }

    private val coroutinScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

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

            val converted = FirebaseDataMapper().convertToDomain(users)

            for(i in 0 until converted.size) {
                coroutinScope.launch(Dispatchers.Main) {

                    converted[i].profileImage = getBitmapOfURL(converted[i].profileImageUrl)

                    if(checkUserProfilePics(converted))
                        GlobalScope.launch {
                            db.saveUsers(converted)
                            callback(converted)
                        }
                }
            }
        }
    }

    private fun checkUserProfilePics(converted: ArrayList<DomainUser>): Boolean{
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
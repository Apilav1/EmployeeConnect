package com.employeeconnect.data.server.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.data.db.EmployeeConnectDb
import com.employeeconnect.domain.Models.User
import com.employeeconnect.data.server.firebase.User as ServerUser
import com.employeeconnect.ui.App
import com.employeeconnect.ui.home.HomeActivity
import kotlinx.coroutines.*
import java.net.URL

class GetUsersPicturesInBitmapsRequest( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                                        private val db: EmployeeConnectDb = EmployeeConnectDb()
) {

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            coroutineScope.launch {
                val errorMessage = App.instance?.getString(R.string.error_loading_message)
                showToast(errorMessage)
            }

        }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

    fun execute(users: ArrayList<ServerUser>, callback: (ArrayList<User>) -> Unit){

        for(i in 0 until users.size) {

            coroutineScope.launch(Dispatchers.Main) {

                users[i].profileImage = getBitmapOfURL(users[i].profileImageUrl)

                if(checkUserProfilePics(users)) {

                    val converted = dataMapper.convertToDomain(users)

                    callback(converted)


                    GlobalScope.launch(Dispatchers.Main) {

                        val converted = dataMapper.convertToDomain(users)

                         db.saveUsers(converted)

                         callback(converted)

                    }
                }
            }
        }

    }

    private fun checkUserProfilePics(converted: ArrayList<ServerUser>): Boolean{
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
package com.employeeconnect.data.Server

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class FirebaseServer(private val dataMapper: FirebaseDataMapper = FirebaseDataMapper()) : DataSource {

    override fun registerNewUser(
        user: User,
        password: String,
        onSuccess: () -> Unit
    ){
        try {
            FirebaseRegisterUserRequest().execute(user, password, onSuccess)
        }
        catch (e : Exception){
            throw(e)
        }
     }

    override fun getUsers(callback: (ArrayList<User>) -> Unit){

        try{

            FirebaseGetUsersRequest().execute(callback)
        }
        catch (e: Exception){
            throw (e)
        }

    }

}
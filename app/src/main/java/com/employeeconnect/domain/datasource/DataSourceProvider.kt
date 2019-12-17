package com.employeeconnect.domain.datasource

import android.util.Log
import com.employeeconnect.data.Server.FirebaseServer
import com.employeeconnect.domain.Models.User
import com.employeeconnect.extensions.firstResult

class DataSourceProvider(private val sources: List<DataSource> = SOURCES) {

    companion object {
        val SOURCES = listOf(FirebaseServer())
    }

    fun registerNewUser(user: User, password: String, onSuccess: () -> Unit) = requestToSources {
        try {
            it.registerNewUser(user, password, onSuccess)
        }
        catch (e: Exception){
            throw (e)
        }
    }

    fun getUser(callback: (ArrayList<User>) -> Unit) = requestToSources{
        try {
            it.getUsers(callback)
        }
        catch (e: Exception){
            throw (e)
        }
    }

    private fun <T : Any> requestToSources(f: (DataSource) -> T?): T = sources.firstResult { f(it) }

}
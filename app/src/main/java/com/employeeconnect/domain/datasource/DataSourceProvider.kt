package com.employeeconnect.domain.datasource

import android.util.Log
import com.employeeconnect.data.Server.FirebaseServer
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
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

    fun getCurrentUserId() = requestToSources{
          it.getCurrentUserId()
    }

    fun fetchCurrentUser() = requestToSources{
          it.fetchCurrentUser()
    }

    fun createChatRoom(usersId: ArrayList<String>, callback: (chatRoomId: String) -> Unit) = requestToSources {
         it.createChatRoom(ChatRoom(usersId), callback)
    }

    fun sendMessage(chatRoomId: String, message: Message) = requestToSources {
        it.sendMessage(chatRoomId, message)
    }

    fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit) = requestToSources {
        it.setMessageListener(chatRoomId, callback)
    }

    fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String) = requestToSources{
        it.addChatRoomIdToUsers(users, chatRoomId)
    }

    private fun <T : Any> requestToSources(f: (DataSource) -> T?): T = sources.firstResult { f(it) }

}
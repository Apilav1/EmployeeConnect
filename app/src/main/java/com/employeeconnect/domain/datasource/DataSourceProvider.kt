package com.employeeconnect.domain.datasource

import com.employeeconnect.data.Server.firebase.FirebaseServer
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

    fun getCurrentUserId(callback: (uid: String?) -> Unit) = requestToSources{
            it.getCurrentUserId(callback)
    }

    fun fetchCurrentUser(callback: (user: User) -> Unit) = requestToSources{
          it.fetchCurrentUser(callback)
    }

    fun createChatRoom(usersId: ArrayList<String>, callback: (chatRoomId: String) -> Unit) = requestToSources {
         it.createChatRoom(ChatRoom("", usersId), callback)
    }

    fun sendMessage(chatRoomId: String, message: Message, callback: () -> Unit) = requestToSources {
        it.sendMessage(chatRoomId, message, callback)
    }

    fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit) = requestToSources {
        it.setMessageListener(chatRoomId, callback)
    }

    fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String, callback: () -> Unit) = requestToSources{
        it.addChatRoomIdToUsers(users, chatRoomId, callback)
    }

    fun getLatestMessages(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit) = requestToSources {
        it.getLatestMessages(chatRooms, callback)
    }

    fun getUserById(userId: String, callback: (User) -> Unit) = requestToSources {
        it.getUserById(userId, callback)
    }

    fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<User>) -> Unit) = requestToSources {
        it.getMultipleUsersById(usersIds, callback)
    }

    fun updateUserCommand(user: User, pictureChaged: Boolean,callback: () -> Unit) = requestToSources {
        it.updateUser(user, pictureChaged, callback)
    }

    fun deleteUser(userId: String, callback: () -> Unit) = requestToSources {
        it.deleteUser(userId, callback)
    }

    fun signInUserWithEmailAndPassword(email: String, password: String, callback: (signInSuccessful: Boolean) -> Unit)
            = requestToSources {
        it.signInUserWithEmailAndPassword(email, password, callback)
    }

    fun logoutUser(callback: () -> Unit) = requestToSources {
        it.logoutUser(callback)
    }

    fun verifyUserProfile(userId: String, callback: () -> Unit) = requestToSources {
        it.verifyUserProfile(userId, callback)
    }

    fun makeUserAModerator(userId: String, callback: () -> Unit) = requestToSources {
        it.makeUserAModerator(userId, callback)
    }

    fun checkIfUserIsVerified(email: String, callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit) = requestToSources {
        it.checkIfUserIsVerified(email, callback)
    }

    fun updateLatestMessages( message: Message, callback: () -> Unit) = requestToSources {
        it.updateLatestMessages(message, callback)
    }

    private fun <T : Any> requestToSources(f: (DataSource) -> T?): T = sources.firstResult { f(it) }

}
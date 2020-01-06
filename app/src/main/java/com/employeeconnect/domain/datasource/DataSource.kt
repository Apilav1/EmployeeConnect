package com.employeeconnect.domain.datasource

import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User

interface DataSource {

    fun registerNewUser(user: User, password: String, onSuccess: () -> Unit)

    fun getUsers(callback: (ArrayList<User>) -> Unit)

    fun getCurrentUserId(callback: (uid: String?) -> Unit)

    fun fetchCurrentUser()

    fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit)

    fun sendMessage(chatRoomId: String, message: Message)

    fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit)

    fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String)

    fun getLatestMessages(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit)

    fun getUserById(userId: String, callback: (User) -> Unit)

    fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<User>) -> Unit)

    fun updateUser(user: User, pictureChanged: Boolean,callback: () -> Unit)

    fun deleteUser(userId: String, callback: () -> Unit)

    fun signInUserWithEmailAndPassword(email: String, password: String, callback: (signInSuccessful: Boolean) -> Unit)

    fun logoutUser(callback: () -> Unit)

}
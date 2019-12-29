package com.employeeconnect.domain.datasource

import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User

interface DataSource{

    fun registerNewUser(user: User, password: String, onSuccess: () -> Unit)

    fun getUsers(callback: (ArrayList<User>) -> Unit)

    fun getCurrentUserId(): String?

    fun fetchCurrentUser()

    fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit)

    fun sendMessage(chatRoomId: String, message: Message)

    fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit)

    fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String)

    fun getLatestMessages(callback: (ArrayList<Message>) -> Unit)

    fun getUserById(userId: String, callback: (User) -> Unit)

    fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<User>) -> Unit)

}
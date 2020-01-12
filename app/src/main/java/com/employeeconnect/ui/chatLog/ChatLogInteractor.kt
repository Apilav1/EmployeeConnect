package com.employeeconnect.ui.chatLog

import android.util.Log
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.AddChatRoomIdToUsersCommand
import com.employeeconnect.domain.commands.CreateChatRoomCommand
import com.employeeconnect.domain.commands.SendMessageCommand
import com.employeeconnect.domain.commands.SetMessagesListenerCommand

class ChatLogInteractor {

    interface onChatLogListener{

        fun onChatRoomCreated(chatRoomId: String)
        fun onMessageListenerSet(message: ArrayList<Message>)
        fun onSendMessageSuccessfully()
        fun onUpdateUsers()

    }

    fun createChatRoom(users: ArrayList<User>, listener: onChatLogListener){

        val userIds = ArrayList<String>()
        users.forEach { userIds.add(it.uid)
            Log.d("CHATTT", "DOBIO id"+userIds) }

        CreateChatRoomCommand(userIds){ newChatRoomId ->

            listener.onChatRoomCreated(newChatRoomId)

        }.execute()
    }

    fun addChatRoomIdToUser(users: ArrayList<User>, chatRoomId: String,  listener: onChatLogListener){

        AddChatRoomIdToUsersCommand(users, chatRoomId) {

            listener.onUpdateUsers()

        }.execute()

    }

    fun setChatRoomListener(chatRoomId: String, listener: onChatLogListener){

        SetMessagesListenerCommand(chatRoomId){ messages ->

            //Because firebase firestore add documents in random order
            messages.sortBy { it.timeStamp }

            listener.onMessageListenerSet(messages)

        }.execute()
    }

    fun sendMessage(chatRoomId: String, message: Message, listener: onChatLogListener){

        SendMessageCommand(chatRoomId, message){

            listener.onSendMessageSuccessfully()

        }.execute()

    }
}
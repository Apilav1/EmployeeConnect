package com.employeeconnect.ui.chatLog

import com.employeeconnect.domain.Models.Message

interface ChatLogView {

    fun onMessageSendSuccessfully()
    fun onChatRoomCreated(id: String)
    fun showError(error: String)
    fun onChatRoomListener(messages: ArrayList<Message>)
    fun onUpdateUsersWithChatRooms()

}
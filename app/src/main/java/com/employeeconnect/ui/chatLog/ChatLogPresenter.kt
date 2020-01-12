package com.employeeconnect.ui.chatLog

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User

class ChatLogPresenter (val chatLogView: ChatLogView?, val chatLogInteractor: ChatLogInteractor): ChatLogInteractor.onChatLogListener{

    fun createChatRoom(users: ArrayList<User>){
        chatLogInteractor.createChatRoom(users, this)
    }

    override fun onChatRoomCreated(chatRoomId: String) {
        chatLogView?.onChatRoomCreated(chatRoomId)
    }

    fun setChatRoomListener(chatRoomId: String){
        chatLogInteractor.setChatRoomListener(chatRoomId, this)
    }

    fun updateUsersChatRooms(users: ArrayList<User>, chatRoomId: String){
        chatLogInteractor.addChatRoomIdToUser(users, chatRoomId, this)
    }

    override fun onUpdateUsers() {
        chatLogView?.onUpdateUsersWithChatRooms()
    }

    override fun onMessageListenerSet(message: ArrayList<Message>) {
        chatLogView?.onChatRoomListener(message)
    }

    fun sendMessage(chatRoomId: String, message: Message){
        chatLogInteractor.sendMessage(chatRoomId, message, this)
    }

    override fun onSendMessageSuccessfully() {
        chatLogView?.onMessageSendSuccessfully()
    }

}
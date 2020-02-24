package com.employeeconnect.data.server.firebase

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.server.firebase.User as ServerUser

import com.employeeconnect.data.server.firebase.Message as ServerMessage
import com.employeeconnect.domain.Models.Message as DomainMessage

import com.employeeconnect.data.server.firebase.ChatRoom as ServerChatRoom
import com.employeeconnect.domain.Models.ChatRoom as DomainChatRoom

class FirebaseDataMapper {

    fun convertToDomain(users: ArrayList<ServerUser>): ArrayList<DomainUser>{

        var usersResult = ArrayList<DomainUser>()

        for(user in users){
            usersResult.add(DomainUser(user.uid, user.username, user.profileImageUrl,null,  user.email,
                            user.githubUsername, user.linkedInUsername, user.skills, user.position, user.position,
                            user.currentProject, user.verified, user.moderator, user.chatRooms))
        }

        return usersResult
    }

    fun convertUserToDomain(user: ServerUser) : DomainUser {

        return DomainUser(user.uid, user.username, user.profileImageUrl, null, user.email, user.githubUsername,
            user.linkedInUsername, user.skills, user.position, user.teamName, user.currentProject, user.verified,
            user.moderator, user.chatRooms)
    }

    fun convertUserToServer(user: DomainUser) : ServerUser {

        return ServerUser(user.uid, user.username, user.profileImageUrl, user.profileImage, user.email, user.githubUsername,
            user.linkedInUsername, user.skills, user.position, user.teamName, user.currentProject, user.verified,
            user.moderator, user.chatRooms)
    }

    fun convertUsersToServer (users: ArrayList<DomainUser>): ArrayList<ServerUser>{

        var usersResult = ArrayList<ServerUser>()

        for(user in users){
            usersResult.add(ServerUser(user.uid, user.username, user.profileImageUrl, user.profileImage, user.email,
                user.githubUsername, user.linkedInUsername, user.skills, user.position, user.position,
                user.currentProject, user.verified, user.moderator, user.chatRooms))
        }

        return usersResult
    }


    fun convertMessageToDomain(message: ServerMessage) : DomainMessage {
        return DomainMessage(message.fromUser, message.toUser, message.text, message.timeStamp, message.chatRoomId, message.seen)
    }

    fun convertMessageToServer(message: DomainMessage) : ServerMessage {
        return ServerMessage(message.fromUser, message.toUser, message.text, message.timeStamp, message.chatRoomId, message.seen)
    }

    fun convertMessagesToDomain(messages: ArrayList<ServerMessage>) : ArrayList<DomainMessage> {

        val result = ArrayList<DomainMessage>()

        messages.forEach { convertMessageToDomain(it) }

        return result
    }

    fun convertMessagesToServer(messages: ArrayList<DomainMessage>) : ArrayList<ServerMessage> {

        val result = ArrayList<ServerMessage>()

        messages.forEach { convertMessageToServer(it) }

        return result
    }

    fun convertServerChatRoomToDomain(chatRoom: ServerChatRoom) : DomainChatRoom {
        return DomainChatRoom(chatRoom.uid, chatRoom.usersId)
    }

    fun convertChatRoomToServer(chatRoom: DomainChatRoom) : ServerChatRoom {
        return ServerChatRoom(chatRoom.uid, chatRoom.usersId)
    }

    fun converteServerChatRoomsToDomain(chatRooms: ArrayList<ServerChatRoom>) : ArrayList<DomainChatRoom> {

        val result = ArrayList<DomainChatRoom>()

        chatRooms.forEach { convertServerChatRoomToDomain(it) }

        return result

    }

    fun converteChatRoomsToServer(chatRooms: ArrayList<DomainChatRoom>) : ArrayList<ServerChatRoom> {

        val result = ArrayList<ServerChatRoom>()

        chatRooms.forEach { convertChatRoomToServer(it) }

        return result

    }
}
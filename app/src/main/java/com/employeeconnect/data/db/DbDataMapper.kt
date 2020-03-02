package com.employeeconnect.data.db

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.db.User as DbUser

import com.employeeconnect.data.db.Message as DbMessage
import com.employeeconnect.domain.Models.Message as DomainMessage

import com.employeeconnect.data.db.ChatRoom as DbChatRoom
import com.employeeconnect.domain.Models.ChatRoom as DomainChatRoom

class DbDataMapper {

    fun convertToDomain(users: ArrayList<DbUser>): ArrayList<DomainUser>{

        val usersResult = ArrayList<DomainUser>()

        for(user in users){
            usersResult.add(DomainUser(user.uid, user.username, user.profileImageUrl, user.profileImage, user.email,
                            user.githubUsername, user.linkedInUsername, user.skills, user.position, user.position,
                            user.currentProject, user.verified, user.moderator, hashMapOf()))
        }

        return usersResult
    }

    fun convertUserToDomain(user: DbUser) : DomainUser {

        return DomainUser(user.uid, user.username, user.profileImageUrl, user.profileImage, user.email, user.githubUsername,
            user.linkedInUsername, user.skills, user.position, user.teamName, user.currentProject, user.verified,
            user.moderator, user.chatRooms)
    }

    fun convertUserToDbModel(user: DomainUser) : DbUser {

        return DbUser(user.uid, user.username, user.profileImageUrl, user.profileImage, user.email, user.githubUsername,
            user.linkedInUsername, user.skills, user.position, user.teamName, user.currentProject, user.verified,
            user.moderator, user.chatRooms)
    }

    fun convertUsersToDbModel (users: ArrayList<DomainUser>): ArrayList<DbUser>{

        val usersResult = ArrayList<DbUser>()

        for(user in users){
            usersResult.add(DbUser(user.uid, user.username, user.profileImageUrl, user.profileImage,  user.email,
                user.githubUsername, user.linkedInUsername, user.skills, user.position, user.teamName,
                user.currentProject, user.verified, user.moderator, user.chatRooms))
        }

        return usersResult
    }


    fun convertMessageToDomain(message: DbMessage) : DomainMessage {
        return DomainMessage(message.fromUser, message.toUser, message.text, message.timestamp, message.chatRoomId, message.seen)
    }

    fun convertMessageToDbModel(message: DomainMessage) : DbMessage {

        return DbMessage(message.fromUser, message.toUser, message.text, message.timeStamp,
                            message.chatRoomId, message.seen)

    }

    fun convertMessagesToDomain(messages: ArrayList<DbMessage>) : ArrayList<DomainMessage> {

        val result = ArrayList<DomainMessage>()

        messages.forEach { result.add(convertMessageToDomain(it))}

        return result
    }

    fun convertMessagesToDbMessage(messages: ArrayList<DomainMessage>) : ArrayList<DbMessage> {

        val result = ArrayList<DbMessage>()

        messages.forEach { result.add(convertMessageToDbModel(it)) }

        return result
    }

    fun convertDbChatRoomToDomain(chatRoom: DbChatRoom) : DomainChatRoom {
        return DomainChatRoom(chatRoom.uid, chatRoom.usersId)
    }

    fun convertChatRoomToDbModel(chatRoom: DomainChatRoom) : DbChatRoom {
        return DbChatRoom(chatRoom.uid, chatRoom.usersId)
    }

    fun converteDbChatRoomsToDomain(chatRooms: ArrayList<DbChatRoom>) : ArrayList<DomainChatRoom> {

        val result = ArrayList<DomainChatRoom>()

        chatRooms.forEach { result.add(convertDbChatRoomToDomain(it) )}

        return result

    }

    fun converteChatRoomsToDbModel(chatRooms: ArrayList<DomainChatRoom>) : ArrayList<DbChatRoom> {

        val result = ArrayList<DbChatRoom>()

        chatRooms.forEach { result.add(convertChatRoomToDbModel(it)) }

        return result

    }
}
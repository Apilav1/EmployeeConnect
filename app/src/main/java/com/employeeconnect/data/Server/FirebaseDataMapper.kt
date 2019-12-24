package com.employeeconnect.data.Server

import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.Server.User as ServerUser

class FirebaseDataMapper {

    fun convertToDomain(users: ArrayList<ServerUser>): ArrayList<DomainUser>{

        var usersResult = ArrayList<DomainUser>()

        for(user in users){
            usersResult.add(DomainUser(user.uid, user.username, user.profileImageUrl, user.email,
                            user.githubUsername, user.linkedInUsername, user.skills, user.position, user.position,
                            user.currentProject, user.verified, user.moderator, user.chatRooms))
        }

        return usersResult
    }
}
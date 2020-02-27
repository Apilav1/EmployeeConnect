package com.employeeconnect.data.db

import android.graphics.Bitmap

data class User (var map: MutableMap<String, Any?>){
    var uid: String by map
    var username: String by map
    var profileImageUrl: String by map
    var profileImage: Bitmap? by map
    var email: String by map
    var githubUsername: String by map
    var linkedInUsername: String by map
    var skills: String by map
    var position: String by map
    var teamName: String by map
    var currentProject: String by map
    var verified: Boolean by map
    var moderator: Boolean by map

    constructor(uid: String, username: String, profileImageUrl: String, profileImage: Bitmap?, email: String, githubUsername: String,
                linkedInUsername: String, skills: String, position: String, teamName: String, currentProject: String,
                verified: Boolean, moderator: Boolean): this(HashMap()){

        this.uid = uid
        this.username = username
        this.profileImageUrl = profileImageUrl
        this.profileImage = profileImage
        this.email = email
        this.githubUsername = githubUsername
        this.linkedInUsername = linkedInUsername
        this.skills = skills
        this.position = position
        this.teamName = teamName
        this.currentProject = currentProject
        this.verified = verified
        this.moderator = moderator

    }
}

data class ChatRoom(var map: MutableMap<String, Any?>){
    val uid: String by map
    val usersId: ArrayList<String> by map
    constructor(): this(HashMap())
}

data class Message(var map: MutableMap<String, Any?>){
    val fromUser: String by map
    val toUser: String by map
    val text: String by map
    val timeStamp: Long by map
    val chatRoomId: String by map
    var seen: Boolean by map

    constructor(): this(HashMap())
}



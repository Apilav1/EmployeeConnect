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
    lateinit var chatRooms: HashMap<String, String> //chatRoom id as key, id of toUser as value

    constructor(uid: String, username: String, profileImageUrl: String, profileImage: Bitmap?, email: String, githubUsername: String,
                linkedInUsername: String, skills: String, position: String, teamName: String, currentProject: String,
                verified: Boolean, moderator: Boolean, chatRooms: HashMap<String, String>): this(HashMap()){

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
        this.chatRooms = chatRooms

    }
}

data class ChatRoom(var map: MutableMap<String, Any?>){
    var uid: String by map
    var usersId: ArrayList<String> by map

    constructor(): this(HashMap())

    constructor(uid: String, usersId: ArrayList<String>): this(HashMap()){
        this.uid = uid
        this.usersId = usersId
    }
}

data class Message(var map: MutableMap<String, Any?>){

    var uid: String by map
    var fromUser: String by map
    var toUser: String by map
    var text: String by map
    var timeStamp: Long by map
    var chatRoomId: String by map
    var seen: Boolean by map

    constructor(): this(HashMap())

    constructor(fromUser: String, toUser: String, text: String,
                timeStamp: Long, chatRoomId: String, seen: Boolean): this(HashMap()){

        this.fromUser = fromUser
        this.toUser = toUser
        this.text = text
        this.timeStamp = timeStamp
        this.chatRoomId = chatRoomId
        this.seen = seen

    }
}



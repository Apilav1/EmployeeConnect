package com.employeeconnect.domain.Models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var uid: String,
    var username: String,
    var profileImageUrl: String,
    var email: String,
    val githubUsername: String,
    val linkedInUsername: String,
    var skills: String,
    var position: String,
    var teamName: String,
    var currentProject: String,
    var verified: Boolean,
    var moderator: Boolean,
    val chatRooms: HashMap<String, String> // chatRoom id and users
): Parcelable {

    constructor(): this("", "", "","",
        "", "", "", "", "", "", false, false, HashMap())

}

@Parcelize
class ChatRoom(val uid: String, val usersId: ArrayList<String>) : Parcelable {
    constructor(): this("", ArrayList())
}

@Parcelize
class Message(val fromUser: String, val toUser: String, val text: String, val timeStamp: Long, val chatRoomId: String): Parcelable{
    constructor(): this("", "", "", 0L, "")
}


class ChatMessage(val id: String, val text: String, val timeStamp: Long){
    constructor(): this("", "", 0L)
}


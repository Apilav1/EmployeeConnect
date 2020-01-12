package com.employeeconnect.data.Server.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var uid: String,
    val username: String,
    var profileImageUrl: String,
    val email: String,
    val githubUsername: String,
    val linkedInUsername: String,
    val skills: String,
    val position: String,
    val teamName: String,
    val currentProject: String,
    val verified: Boolean,
    val moderator: Boolean,
    val chatRooms: HashMap<String, String>
): Parcelable{
    constructor(): this("", "", "","",
        "", "", "", "", "", "", false, false, HashMap())
}

@Parcelize
class ChatRoom(val uid: String, val usersId: ArrayList<String>) : Parcelable {
    constructor(): this("", ArrayList())
}

@Parcelize
class Message(val fromUser: String, val toUser: String, val text: String, val timeStamp: Long,
              val chatRoomId: String, var seen: Boolean): Parcelable{
    constructor(): this("", "", "", 0L, "", false)
}

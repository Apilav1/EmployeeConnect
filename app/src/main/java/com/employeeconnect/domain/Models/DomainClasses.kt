package com.employeeconnect.domain.Models

import android.os.Parcel
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
    val chatRooms: HashMap<String, String> // chatRoom id and users
): Parcelable {

    constructor(): this("", "", "","",
        "", "", "", "", "", "", false, false, HashMap())

}

@Parcelize
class ChatRoom(val usersId: ArrayList<String>) : Parcelable {
    constructor(): this(ArrayList())
}

@Parcelize
class Message(val fromUser: String, val toUser: String, val text: String, val timeStamp: Long): Parcelable{
    constructor(): this("", "", "", 0L)
}


class ChatMessage(val id: String, val text: String, val timeStamp: Long){
    constructor(): this("", "", 0L)
}


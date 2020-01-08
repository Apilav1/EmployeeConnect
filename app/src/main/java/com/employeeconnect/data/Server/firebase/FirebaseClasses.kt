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

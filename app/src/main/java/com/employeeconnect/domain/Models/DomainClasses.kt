package com.employeeconnect.domain.Models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


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
    val moderator: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    constructor(): this("", "", "","",
        "", "", "", "", "", "", false, false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(username)
        parcel.writeString(profileImageUrl)
        parcel.writeString(email)
        parcel.writeString(githubUsername)
        parcel.writeString(linkedInUsername)
        parcel.writeString(skills)
        parcel.writeString(position)
        parcel.writeString(teamName)
        parcel.writeString(currentProject)
        parcel.writeByte(if (verified) 1 else 0)
        parcel.writeByte(if (moderator) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
package com.employeeconnect.data.db

object EmployeeTable {
    const val NAME = "EMPLOYEES"
    const val ID = "uid"
    const val USERNAME = "username"
    const val PROFILE_IMAGE_URL = "profileImageUrl"
    const val PROFILE_IMAGE = "profileImage"
    const val EMAIL = "email"
    const val GITHUB_USERNAME = "githubUsername"
    const val LINKED_IN_USERNAME = "linkedInUsername"
    const val SKILLS = "skills"
    const val POSITION = "position"
    const val TEAM_NAME = "teamName"
    const val CURRENT_PROJECT = "currentProject"
    const val VERIFIED = "verified"
    const val MODERATOR = "moderator"
}

object EmployeeChatRoomTable{
    const val NAME = "EMPLOYEE_CHATROOM"
    const val ID = "_id"
    const val EMPLOYEE_ID = "employee_id"
    const val CHATROOM_ID = "chatroom_id"
    const val TO_USER_ID = "toUser_id"
}

object ChatRoomTable {
    const val NAME = "CHATROOM"
    const val ID = "_id"
}

object MessagesTable {
    const val NAME = "MESSAGES"
    const val ID = "uid"
    const val FROM_USER_ID = "fromUser"
    const val TO_USER_ID = "toUser"
    const val TEXT = "text"
    const val TIMESTAMP = "timestamp"
    const val CHATROOM_ID = "chatRoomId"
    const val SEEN  = "seen"
}

object LatestMessagesTable {
    const val NAME = "LATEST_MESSAGES"
    const val ID = "uid"
    const val FROM_USER_ID = "fromUser"
    const val TO_USER_ID = "toUser"
    const val TEXT = "text"
    const val TIMESTAMP = "timestamp"
    const val CHATROOM_ID = "chatRoomId"
    const val SEEN  = "seen"
}

object AppInfo {
    const val NAME = "AppInfo"
    const val CURRENT_USER_ID = "currentUser_id"
}
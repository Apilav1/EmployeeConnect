package com.employeeconnect.domain.commands

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class AddChatRoomIdToUsersCommand( private val users: ArrayList<User>, private val chatRoomId: String,
                            private val dataSourceProvider: DataSourceProvider = DataSourceProvider()): Command<Unit> {

    override fun execute() {
            Log.d("CHATTT", "SALJEM")
            dataSourceProvider.addChatRoomIdToUsers(users, chatRoomId)
    }
}
package com.employeeconnect.domain.commands

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class AddChatRoomIdToUsersCommand( private val users: ArrayList<User>, private val chatRoomId: String,
                                   private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                   private val onSuccess: () -> Unit): Command<Unit> {

    override fun execute() {
            dataSourceProvider.addChatRoomIdToUsers(users, chatRoomId, onSuccess)
    }
}
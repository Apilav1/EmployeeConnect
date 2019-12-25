package com.employeeconnect.domain.commands

import android.util.Log
import com.employeeconnect.domain.datasource.DataSourceProvider

class AddChatRoomIdToUsersCommand( private val usersIds: ArrayList<String>, private val chatRoomId: String,
                            private val dataSourceProvider: DataSourceProvider = DataSourceProvider()): Command<Unit> {

    override fun execute() {
            Log.d("CHATTT", "SALJEM")
            dataSourceProvider.addChatRoomIdToUsers(usersIds, chatRoomId)
    }
}
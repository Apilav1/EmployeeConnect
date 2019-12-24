package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class CreateChatRoomCommand( private val usersId: ArrayList<String>,
                            private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                            private val onSuccess: (chatRoomId: String) -> Unit) : Command<Unit> {

    override fun execute() {
         dataSourceProvider.createChatRoom(usersId, onSuccess)
    }
}
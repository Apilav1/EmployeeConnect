package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.datasource.DataSourceProvider

class SendMessageCommand(private val chatRoomId: String, private val message: Message,
                         private val dataSourceProvider: DataSourceProvider = DataSourceProvider()) : Command<Unit>  {

    override fun execute() {
         dataSourceProvider.sendMessage(chatRoomId, message)
    }
}
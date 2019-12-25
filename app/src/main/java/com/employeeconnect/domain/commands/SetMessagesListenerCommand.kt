package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.datasource.DataSourceProvider

class SetMessagesListenerCommand(private val chatRoomId: String,
                                 private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                 private val callback: (ArrayList<Message>) -> Unit
                                 ) : Command<Unit>{

    override fun execute() {
          dataSourceProvider.setMessageListener(chatRoomId, callback)
    }
}



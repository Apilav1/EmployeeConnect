package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.datasource.DataSourceProvider

class GetLatestMessagesCommand(private val chatRooms: ArrayList<String>,
                                private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                private val callback: (ArrayList<Message>) -> Unit): Command<Unit>  {

    override fun execute() {
            dataSourceProvider.getLatestMessages(chatRooms, callback)
    }
}
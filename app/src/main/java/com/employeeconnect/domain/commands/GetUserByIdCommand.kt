package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class GetUserByIdCommand(private val userId: String,
                         private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                         private val callback: (User) -> Unit) : Command<Unit>  {

    override fun execute() {
        dataSourceProvider.getUserById(userId, callback)
    }
}
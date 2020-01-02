package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.datasource.DataSourceProvider

class LogoutUserCommand(private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                        private val callback: () -> Unit) : Command<Unit>{

    override fun execute() {
        dataSourceProvider.logoutUser(callback)
    }
}
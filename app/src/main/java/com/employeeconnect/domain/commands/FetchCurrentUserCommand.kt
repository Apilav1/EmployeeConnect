package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class FetchCurrentUserCommand(private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                              private val callback: (user: User) -> Unit) : Command<Unit> {

    override fun execute() {
        return dataSourceProvider.fetchCurrentUser(callback)
    }

}
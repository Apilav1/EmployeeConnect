package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class MakeUserAModeratorCommand(private val userId: String,
                                private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                private val callback: () -> Unit): Command<Unit>  {

    override fun execute() {
        dataSourceProvider.makeUserAModerator(userId, callback)
    }
}
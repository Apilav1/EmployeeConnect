package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class GetCurrentUserIdCommand (private val dataSourceProvider: DataSourceProvider = DataSourceProvider()) : Command<String?> {

    override fun execute(): String? {
        return dataSourceProvider.getCurrentUserId()
    }

}
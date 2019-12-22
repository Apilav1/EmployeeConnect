package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class VerifyUserIsLoggedInCommand (private val dataSourceProvider: DataSourceProvider = DataSourceProvider()) : Command<Boolean> {

    override fun execute(): Boolean {
        return dataSourceProvider.verifyUserIsLoggedIn()
    }

}
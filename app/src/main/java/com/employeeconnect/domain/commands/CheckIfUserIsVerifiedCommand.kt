package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class CheckIfUserIsVerifiedCommand(private val email: String,
                                   private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                   private val callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit): Command<Unit>  {

    override fun execute() {
        dataSourceProvider.checkIfUserIsVerified(email, callback)
    }
}
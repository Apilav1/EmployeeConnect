package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider
import java.lang.Exception

class RegisterUserCommand (private val user: User,
                           private val password: String,
                           private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                           private val onSuccess: () -> Unit) : Command<Unit>{
    override fun execute() {
        try {
            return dataSourceProvider.registerNewUser(user, password, onSuccess)
        }
        catch (e: Exception){
            throw(e)
        }
    }
}
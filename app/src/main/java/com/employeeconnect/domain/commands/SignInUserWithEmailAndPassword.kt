package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class SignInUserWithEmailAndPassword(private val email: String,
                                     private val password: String,
                                     private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                     private val callback: (signInSuccessful: Boolean) -> Unit) : Command<Unit> {

    override fun execute() {
            dataSourceProvider.signInUserWithEmailAndPassword(email, password, callback)
    }
}
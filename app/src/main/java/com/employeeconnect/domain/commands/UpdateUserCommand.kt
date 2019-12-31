package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class UpdateUserCommand(private val user: User,
                        private val pictureChaged: Boolean,
                        private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                        private val callback: () -> Unit) : Command<Unit> {

    override fun execute() {
            dataSourceProvider.updateUserCommand(user, pictureChaged, callback)
    }
}
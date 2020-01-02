package com.employeeconnect.domain.commands
import com.employeeconnect.domain.datasource.DataSourceProvider

class DeleteUserCommand(private val userId: String,
                        private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                        private val callback: () -> Unit) : Command<Unit>  {
    override fun execute() {
         dataSourceProvider.deleteUser(userId, callback)
    }
}
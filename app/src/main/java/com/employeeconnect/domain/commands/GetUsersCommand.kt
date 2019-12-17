package com.employeeconnect.domain.commands

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSourceProvider

class GetUsersCommand( private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                                    private val callback: (ArrayList<User>) -> Unit)
                                : Command<Unit> {

    override fun execute(){
         dataSourceProvider.getUser(callback)
    }
}
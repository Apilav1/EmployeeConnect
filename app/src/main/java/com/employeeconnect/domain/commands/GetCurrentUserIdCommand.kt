package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class GetCurrentUserIdCommand (private val dataSourceProvider: DataSourceProvider = DataSourceProvider(),
                               private val callback: (uid: String?)->Unit) : Command<Unit> {

    override fun execute(){
        return dataSourceProvider.getCurrentUserId(callback)
    }

}
package com.employeeconnect.domain.commands

import com.employeeconnect.domain.datasource.DataSourceProvider

class FindCorrectRoomComman(private val dataSourceProvider: DataSourceProvider = DataSourceProvider()) : Command<Unit> {

    override fun execute() {

    }
}
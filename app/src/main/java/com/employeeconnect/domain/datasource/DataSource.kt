package com.employeeconnect.domain.datasource

import com.employeeconnect.domain.Models.User

interface DataSource{

    fun registerNewUser(user: User, password: String, onSuccess: () -> Unit)

    fun getUsers(callback: (ArrayList<User>) -> Unit)

}
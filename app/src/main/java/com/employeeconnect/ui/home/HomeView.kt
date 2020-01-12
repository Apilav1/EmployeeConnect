package com.employeeconnect.ui.home

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User

interface HomeView {

    fun onFetchCurrentUser(user: User)
    fun noUserIsLoggedInError()
    fun showUsers(users: ArrayList<User>)
    fun showError(error: String)
    fun onFetchingLatestMessages(result: ArrayList<Message>)
    fun onFetchingMultipleUsersByIds(result: ArrayList<User>)
    fun onVerificationSuccess()
    fun onMakingUserAModeratorSuccess()
    fun showLatestMessageUpdated()
    fun onUpdateUserSuccefully()
    fun onDeletionUser()
    fun onLogoutUser()
}
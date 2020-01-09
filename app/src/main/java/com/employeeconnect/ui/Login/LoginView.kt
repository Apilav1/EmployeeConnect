package com.employeeconnect.ui.Login

interface LoginView {

    fun showProgress()
    fun hideProgress()
    fun showLoginInfoError()
    fun showUnverifiedProfileError()
    fun navigateToHome()
    fun navigateToRegistration()
    fun showInvalidedEmailError()
    fun makeLoginButtonUnclickable()
    fun makeLoginButtonClickable()

}
package com.employeeconnect.ui.login

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
    fun userIsLoggedIn()

}
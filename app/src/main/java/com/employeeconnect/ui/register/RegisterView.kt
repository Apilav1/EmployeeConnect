package com.employeeconnect.ui.register

interface RegisterView {

    fun showSuccessfulRegistrationMessage()
    fun showError(error: String)
    fun onValidationSuccess()

}
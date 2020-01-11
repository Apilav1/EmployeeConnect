package com.employeeconnect.ui.register

import androidx.fragment.app.Fragment

interface RegisterView {

    fun showSuccessfulRegistrationMessage()
    fun showError(error: String)
    fun onValidationSuccess()
}
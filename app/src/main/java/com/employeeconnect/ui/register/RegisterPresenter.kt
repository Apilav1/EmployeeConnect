package com.employeeconnect.ui.register

import com.employeeconnect.domain.Models.User


class RegisterPresenter (var registeView: RegisterView?, val registerInteractor: RegisterInteractor) :
            RegisterInteractor.OnRegisterFinishedListener {


    fun completeRegistration(user: User, password: String){
            registerInteractor.completeRegistration(user, password, this)
    }

    override fun onSuccess() {
        registeView?.apply {
            showSuccessfulRegistrationMessage()
        }
    }

    override fun onError(error: String) {
        registeView?.apply {
            showError(error)
        }
    }

    fun basicInfoValidation(username: String, email: String, password: String, isPhotoSet: Boolean){
        registerInteractor.basicInfoValidation(username, email, password, isPhotoSet, this)
    }

    fun contactInfoValidation(githubUsername: String, linkedInUsername: String, skills: String){
        registerInteractor.contactInfoValidation(githubUsername, linkedInUsername, skills, this)
    }

    fun employeeInfoValidation(position: String, teamName: String, currentProject: String){
        registerInteractor.employeeInfoValidation(position, teamName, currentProject, this)
    }

    override fun onSuccessValidation() {
        registeView?.apply {
            onValidationSuccess()
        }
    }
}
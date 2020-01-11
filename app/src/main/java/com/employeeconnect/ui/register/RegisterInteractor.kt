package com.employeeconnect.ui.register

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.RegisterUserCommand
import com.employeeconnect.extensions.isEmailValid
import com.employeeconnect.extensions.isPasswordValid
import com.employeeconnect.ui.login.LoginInteractor

class RegisterInteractor {

    interface OnRegisterFinishedListener{
        fun onSuccess()
        fun onError(error: String)
        fun onSuccessValidation()
    }

     fun completeRegistration(user: User, password: String, listener: OnRegisterFinishedListener){

        try {
            RegisterUserCommand(user, password){
                 listener.onSuccess()
            }.execute()
        }
        catch (e: Exception){
            listener.onError(e.message.toString())
        }
    }

    fun basicInfoValidation(username: String, email: String, password: String, isPhotoSet: Boolean
                            , listener: OnRegisterFinishedListener){
        if(username.isEmpty()) {
            listener.onError("Please enter your username")
        }
        else if(!email.isEmailValid()) {
            listener.onError("Please enter valid email")
        }
        else if(!password.isPasswordValid()){
            listener.onError("Password must be longer than 7 characters")
        }
        else if(!isPhotoSet){
            listener.onError("Please insert your profile photo")
        }
        else{
            listener.onSuccessValidation()
            return
        }
    }

    fun contactInfoValidation(githubUsername: String, linkedInUsername: String, skills: String, listener: OnRegisterFinishedListener){
        if(githubUsername.isEmpty()){
            listener.onError("Please enter valid Github username")
        }
        else if(linkedInUsername.isEmpty()){
            listener.onError("Please enter valid LinkedIn username")
        }
        else if(skills.isEmpty()){
            listener.onError("Please enter at least one of your's skills in programming field")
        }
        else{
            listener.onSuccessValidation()
            return
        }
    }

    fun employeeInfoValidation(position: String, teamName: String, currentProject: String, listener: OnRegisterFinishedListener){
        if(position.isEmpty() || teamName.isEmpty() || currentProject.isEmpty()){
            listener.onError("Please enter required information")
        }
        else{
            listener.onSuccessValidation()
        }
    }
}
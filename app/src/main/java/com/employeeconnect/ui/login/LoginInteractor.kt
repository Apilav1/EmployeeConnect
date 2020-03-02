package com.employeeconnect.ui.login

import android.util.Log
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.CheckIfUserIsVerifiedByEmailCommand
import com.employeeconnect.domain.commands.FetchCurrentUserCommand
import com.employeeconnect.domain.commands.GetCurrentUserIdCommand
import com.employeeconnect.domain.commands.SignInUserWithEmailAndPassword
import com.employeeconnect.extensions.isEmailValid

class LoginInteractor {

    interface OnLoginFinishedListener {

        fun onSuccess()
        fun onInvalidEmailError()
        fun onInvalidInfoError()
        fun onUnverifiedProfileError()
        fun onUserLoggedIn()

    }

    fun verifyUserIsLoggedIn(listener: OnLoginFinishedListener) {

        GetCurrentUserIdCommand{result->
            if(result != null)
                listener.onUserLoggedIn()

        }.execute()
    }

    fun performLogin(email: String, password: String, listener: OnLoginFinishedListener){

        if(!email.isEmailValid()) {
            listener.onInvalidEmailError()
            return
        }

        CheckIfUserIsVerifiedByEmailCommand(email){ exists, isVerified ->

            if(!exists){
                listener.onInvalidEmailError()
                return@CheckIfUserIsVerifiedByEmailCommand
            }

            if(isVerified){
                SignInUserWithEmailAndPassword(email, password){ signInSuccessful ->
                    if(signInSuccessful){
                           listener.onSuccess()
                           return@SignInUserWithEmailAndPassword
                    }
                    else{
                          listener.onInvalidInfoError()
                          return@SignInUserWithEmailAndPassword
                    }
                }.execute()
            }
            else{
                listener.onUnverifiedProfileError()
                return@CheckIfUserIsVerifiedByEmailCommand
            }
        }.execute()
    }
}

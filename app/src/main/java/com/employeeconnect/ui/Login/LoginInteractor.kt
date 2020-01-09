package com.employeeconnect.ui.Login

import android.util.Log
import com.employeeconnect.domain.commands.CheckIfUserIsVerifiedByEmailCommand
import com.employeeconnect.domain.commands.SignInUserWithEmailAndPassword
import com.employeeconnect.extensions.isEmailValid

class LoginInteractor {

    interface OnLoginFinishedListener {
        fun onSuccess()
        fun onInvalidEmailError()
        fun onInvalidInfoError()
        fun onUnverifiedProfileError()
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

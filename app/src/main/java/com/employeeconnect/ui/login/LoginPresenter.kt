package com.employeeconnect.ui.login

class LoginPresenter(var loginView: LoginView?, val loginInteractor: LoginInteractor) :
    LoginInteractor.OnLoginFinishedListener{

    fun performLogin(email: String, password: String){
         loginView?.showProgress()
         loginView?.makeLoginButtonUnclickable()
         loginInteractor.performLogin(email, password, this)
    }

    override fun onSuccess() {
        loginView?.apply {
            navigateToHome()
            hideProgress()
        }
    }

    override fun onInvalidEmailError() {
        loginView?.apply {
            showInvalidedEmailError()
            hideProgress()
            makeLoginButtonClickable()
        }
    }

    override fun onInvalidInfoError() {
        loginView?.apply {
            showInvalidedEmailError()
            hideProgress()
            makeLoginButtonClickable()
        }
    }

    override fun onUnverifiedProfileError() {
        loginView?.apply {
            showUnverifiedProfileError()
            hideProgress()
            makeLoginButtonClickable()
        }
    }

    fun navigateToRegistration(){
        loginView?.apply {
            navigateToRegistration()
        }
    }

    fun onDestroy() {
        loginView = null
    }
}
package com.employeeconnect.ui.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.commands.CheckIfUserIsVerifiedByEmailCommand
import com.employeeconnect.domain.commands.SignInUserWithEmailAndPassword
import com.employeeconnect.extensions.isEmailValid
import com.employeeconnect.ui.Activities.HomeActivity
import com.employeeconnect.ui.Activities.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this, LoginInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide()

        register_textview_login.setOnClickListener { presenter.navigateToRegistration() }

        button_login.setOnClickListener {

            val email = email_login.text.toString()
            val password = password_login.text.toString()

            performLogin(email, password)

        }
    }

    private fun performLogin(email: String, password: String){
        presenter.performLogin(email, password)
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showLoginInfoError() {
        Toast.makeText(applicationContext, "Please enter a valid info!", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun navigateToRegistration() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun showUnverifiedProfileError() {

        val company = resources.getString(R.string.company_name)

        Toast.makeText(applicationContext,
            "Your user profile is not verified yet by $company's team, please try later!",
                    Toast.LENGTH_LONG).show()
    }

    override fun showInvalidedEmailError() {
        Toast.makeText(applicationContext, "Please enter a valid email!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy(){
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun makeLoginButtonClickable() {
        button_login.isClickable = true
    }

    override fun makeLoginButtonUnclickable() {
        button_login.isClickable = false
    }

}

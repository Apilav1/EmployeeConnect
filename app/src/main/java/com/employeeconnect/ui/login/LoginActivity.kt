package com.employeeconnect.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.employeeconnect.R
import com.employeeconnect.ui.activities.BaseActivity
import com.employeeconnect.ui.home.EmployeesFragment
import com.employeeconnect.ui.home.HomeActivity
import com.employeeconnect.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this, LoginInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide()

        presenter.verifyUserIsLoggedIn()

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
        HomeActivity.currentFragmet = EmployeesFragment()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun navigateToRegistration() {
        if(BaseActivity.deviceIsConnected)
            startActivity(Intent(this, RegisterActivity::class.java))
        else
            Toast.makeText(applicationContext, "Registration is not supported in a offline mode", Toast.LENGTH_LONG).show()
    }

    override fun showUnverifiedProfileError() {

        val company = resources.getString(R.string.company_name)

        Toast.makeText(applicationContext,
            "Your user profile is not verified yet by $company's team, please try later!",
                    Toast.LENGTH_LONG).show()
    }

    override fun showInvalidedEmailError() {
        Toast.makeText(applicationContext, "Please enter a correct email!", Toast.LENGTH_SHORT).show()
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

    override fun userIsLoggedIn() {
        navigateToHome()
    }

}

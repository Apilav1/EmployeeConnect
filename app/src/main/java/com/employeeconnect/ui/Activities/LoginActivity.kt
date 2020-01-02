package com.employeeconnect.ui.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.commands.SignInUserWithEmailAndPassword
import com.employeeconnect.extensions.isEmailValid
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide()

        register_textview_login.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        button_login.setOnClickListener {
            val email = email_login.text.toString()
            val password = password_login.text.toString()

            if(!email.isEmailValid()){
                Toast.makeText(applicationContext, "Please enter a valid email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SignInUserWithEmailAndPassword(email, password){ signInSuccessful ->

                if(signInSuccessful){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(applicationContext, "Please enter a valid info!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

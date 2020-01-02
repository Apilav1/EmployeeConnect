package com.employeeconnect.ui.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.employeeconnect.R
import com.employeeconnect.domain.commands.SignInUserWithEmailAndPassword
import com.employeeconnect.extensions.isEmailValid
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getSupportActionBar()?.hide()
        gif_imageView_login.alpha = 0.0f

        register_textview_login.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        button_login.setOnClickListener {

            gif_imageView_login.alpha = 1.0f
            Glide.with(this).load(R.drawable.loading).into(gif_imageView_login);

            Log.d("CHATTT", "LOGIN PRESSED")
            val email = email_login.text.toString()
            val password = password_login.text.toString()

            if(!email.isEmailValid()){
                Toast.makeText(applicationContext, "Please enter a valid email!", Toast.LENGTH_SHORT).show()
                Log.d("CHATTT", "email not valid")
                return@setOnClickListener
            }

            SignInUserWithEmailAndPassword(email, password){ signInSuccessful ->

                if(signInSuccessful){
                    Log.d("CHATTT", "SIGN in successful")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Log.d("CHATTT", "SIGN not successful")
                    Toast.makeText(applicationContext, "Please enter a valid info!", Toast.LENGTH_SHORT).show()
                }

                gif_imageView_login.alpha = 0.0f
            }.execute()
        }
    }
}

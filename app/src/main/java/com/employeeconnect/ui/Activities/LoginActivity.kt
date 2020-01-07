package com.employeeconnect.ui.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.employeeconnect.R
import com.employeeconnect.domain.commands.CheckIfUserIsVerifiedByEmailCommand
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

            button_login.isClickable = false

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

            CheckIfUserIsVerifiedByEmailCommand(email){ exists, isVerified ->

                if(!exists){
                    Toast.makeText(applicationContext, "Please enter a valid info!", Toast.LENGTH_SHORT).show()
                    gif_imageView_login.alpha = 0.0f
                    button_login.isClickable = true
                    return@CheckIfUserIsVerifiedByEmailCommand
                }

                if(isVerified){

                    SignInUserWithEmailAndPassword(email, password){ signInSuccessful ->
                        if(signInSuccessful){
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        else{
                            Log.d("CHATTT", "SIGN not successful")
                            Toast.makeText(applicationContext, "Please enter a valid info!", Toast.LENGTH_SHORT).show()
                        }
                        gif_imageView_login.alpha = 0.0f
                        button_login.isClickable = true
                    }.execute()
                }
                else{
                    val company = resources.getString(R.string.company_name)

                    Toast.makeText(applicationContext,
                        "Your user profile is not verified yet by $company's team, please try later!",
                        Toast.LENGTH_LONG).show()

                    button_login.isClickable = true
                    gif_imageView_login.alpha = 0.0f
                }

            }.execute()
        }
    }
}

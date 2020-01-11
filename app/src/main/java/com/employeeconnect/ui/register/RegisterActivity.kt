package com.employeeconnect.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.RegisterUserCommand
import kotlinx.android.synthetic.main.fragment_basic_info_register.*
import kotlinx.android.synthetic.main.fragment_contact_info_register.*
import kotlinx.android.synthetic.main.fragment_employee_info_register.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class RegisterActivity : AppCompatActivity(), RegisterView,
                                            BasicInfoRegisterFragment.OnBasicInfoRegisterFragmentInteractionListener,
                                            ContactInfoRegisterFragment.OnContactInfoRegisterFragmentInteractionListener,
                                            EmployeeInfoRegisterFragment.OnEmployeeInfoRegisterFragmentInteractionListener{

    private val presenter = RegisterPresenter(this, RegisterInteractor())
    private lateinit var nextFragment: Fragment
    private var finalFragmentIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        replaceFragment(BasicInfoRegisterFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_containter_register, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBasicInfoRegisterFragmentInteraction(view: View, username: String,
                                                        email: String, password: String, isPhotoSet: Boolean) {
        when(view.id){
            R.id.back_button_basic_register -> finish()
            R.id.next_button_basic_register -> {
                nextFragment = ContactInfoRegisterFragment()
                presenter.basicInfoValidation(username, email, password, isPhotoSet)
            }
        }
    }

    override fun onContactInfoRegisterFragmentInteraction(view: View, githubUsername: String,
                                                          linkedInUsername: String, skills: String) {
        when(view.id){
            R.id.back_button_contact_info_register -> {
                supportFragmentManager.popBackStackImmediate()
                nextFragment = ContactInfoRegisterFragment()
            }
            R.id.next_button_contact_info_register -> {
                nextFragment = EmployeeInfoRegisterFragment()
                presenter.contactInfoValidation(githubUsername, linkedInUsername, skills)
            }
        }
    }

    override fun onEmployeeInfoRegisterFragmentInteraction(view: View, position: String, teamName: String, currentProject: String) {
        when(view.id){
            R.id.back_button_employee_info_register -> {
                supportFragmentManager.popBackStackImmediate()
                nextFragment = EmployeeInfoRegisterFragment()
            }
            R.id.complete_registration_button_register -> {
                finalFragmentIsShown = true
                presenter.employeeInfoValidation(position, teamName, currentProject)
            }
        }
    }

    override fun onValidationSuccess() {
        if(!finalFragmentIsShown){
            replaceFragment(nextFragment)
        }
        else{
            val user = User("", username_register.text.toString(),  BasicInfoRegisterFragment.selectedPhotoUriString,
                email_register.text.toString(), github_username_register.text.toString(),
                linkenin_link_register.text.toString(), skills_register.text.toString(),
                position_register.text.toString(), team_register.text.toString(),
                "", false, false, HashMap())

            val password = password_register.text.toString()

            presenter.completeRegistration(user, password)
        }
    }

    override fun showSuccessfulRegistrationMessage() {

            val company = resources.getString(R.string.company_name)
            Toast.makeText(applicationContext, "Your registration is complete! New account will be soon" +
                    " validated by ${company}'s stuff", Toast.LENGTH_LONG).show()

            Thread.sleep(500)
            finish()
    }

    override fun showError(error: String) {
        if(finalFragmentIsShown)
           (nextFragment as EmployeeInfoRegisterFragment).hideProgress()
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }
}

package com.employeeconnect.ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.employeeconnect.ui.Fragments.BasicInfoRegisterFragment
import com.employeeconnect.ui.Fragments.ContactInfoRegisterFragment
import com.employeeconnect.ui.Fragments.EmployeeInfoRegisterFragment
import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.RegisterUserCommand
import kotlinx.android.synthetic.main.fragment_basic_info_register.*
import kotlinx.android.synthetic.main.fragment_contact_info_register.*
import kotlinx.android.synthetic.main.fragment_employee_info_register.*

class RegisterActivity : AppCompatActivity(),
                                            BasicInfoRegisterFragment.OnBasicInfoRegisterFragmentInteractionListener,
                                            ContactInfoRegisterFragment.OnContactInfoRegisterFragmentInteractionListener,
                                            EmployeeInfoRegisterFragment.OnEmployeeInfoRegisterFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        replaceFragment(BasicInfoRegisterFragment())

    }

    fun View.setMarginTop(topMargin: Int) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.bottomMargin)
        layoutParams = params
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_containter_register, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBasicInfoRegisterFragmentInteraction(view: View) {
        when(view.id){
            R.id.back_button_basic_register -> finish()
            R.id.next_button_basic_register -> replaceFragment(ContactInfoRegisterFragment())
        }
    }

    override fun onContactInfoRegisterFragmentInteraction(view: View) {
        when(view.id){
            R.id.back_button_contact_info_register -> supportFragmentManager.popBackStackImmediate()
            R.id.next_button_contact_info_register -> replaceFragment(EmployeeInfoRegisterFragment())
        }
    }

    override fun onEmployeeInfoRegisterFragmentInteraction(view: View) {
        when(view.id){
            R.id.back_button_employee_info_register -> supportFragmentManager.popBackStackImmediate()
            R.id.complete_registration_button_register -> completeRegistration()
        }
    }

    private fun completeRegistration(){
        val user = User("", username_register.text.toString(),  BasicInfoRegisterFragment.selectedPhotoUriString,
                            email_register.text.toString(), github_username_register.text.toString(),
                            linkenin_link_register.text.toString(), skills_register.text.toString(),
                            position_register.text.toString(), team_register.text.toString(),
                "", false, false, HashMap())

        try {
            RegisterUserCommand(user, password_register.text.toString()){

                val company = resources.getString(R.string.company_name)
                Toast.makeText(applicationContext, "Your registration is complete! New account will be soon" +
                        " validated by ${company}'s stuff", Toast.LENGTH_LONG).show()

                Thread.sleep(500)
                finish()
            }.execute()
        }
        catch (e: Exception){
            Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

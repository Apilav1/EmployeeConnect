package com.employeeconnect.ui.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.employeeconnect.extensions.isEmailValid
import com.employeeconnect.extensions.isPasswordValid
import com.employeeconnect.extensions.revealAndDropAnimation

import com.employeeconnect.R
import kotlinx.android.synthetic.main.fragment_basic_info_register.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BasicInfoRegisterFragment.OnBasicInfoRegisterFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BasicInfoRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BasicInfoRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listenerBasicInfoRegister: OnBasicInfoRegisterFragmentInteractionListener? = null
    private var fragmentContainer: ViewGroup? = null
    private var REQUESTCODE_PHOTO = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_basic_info_register, container, false)

        fragmentContainer = container


        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentContainer.revealAndDropAnimation(listOf<View>(
            photo_button_register,
            username_register,
            email_register,
            password_register,
            back_button_basic_register,
            next_button_basic_register
        ))

        back_button_basic_register?.setOnClickListener {
            onButtonPressed(it)
        }

        next_button_basic_register?.setOnClickListener {
            if(basicInfoValidation()) onButtonPressed(it)
            else return@setOnClickListener
        }

        photo_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUESTCODE_PHOTO)
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(view: View) {
        listenerBasicInfoRegister?.onBasicInfoRegisterFragmentInteraction(view)
    }

    fun basicInfoValidation(): Boolean{

        val username = username_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if(username.isEmpty()) {
            Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!email.isEmailValid()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!password.isPasswordValid(password)){
            Toast.makeText(context, "Password must be longer than 7 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        if(photo_button_register.alpha != 0.0f){
            Toast.makeText(context, "Please insert your profile photo", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUESTCODE_PHOTO && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedPhotoUri)
            picture_user_profile_register.setImageBitmap(bitmap)
            photo_button_register.alpha = 0f

            selectedPhotoUriString = selectedPhotoUri.toString()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBasicInfoRegisterFragmentInteractionListener) {
            listenerBasicInfoRegister = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerBasicInfoRegister = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnBasicInfoRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onBasicInfoRegisterFragmentInteraction(view: View)
    }

    companion object {
        lateinit var selectedPhotoUriString: String
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BasicInfoRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BasicInfoRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

package com.employeeconnect.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.employeeconnect.extensions.convertBitmapToUri
import com.employeeconnect.ui.App
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_basic_info_register.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserProfileFragment.OnUserProfileFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listenerUserProfile: OnUserProfileFragmentInteractionListener? = null
    private var currentUser: User? = null
    private var pictureIsChanged = false
    private var REQUESTCODE_PHOTO = 0
    private lateinit var myView: View

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
        myView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        return myView
    }

    override fun onStart() {
        super.onStart()

        setUserInfo()

        setListeners()
    }

    fun setUserInfo(){

        currentUser = HomeActivity.currentUser

        myView.username_user_profile.setText(currentUser!!.username)
        myView.email_user_profile.setText(currentUser!!.email)
        myView.skills_user_profile.setText(currentUser!!.position)
        myView.currentProject_user_profile.setText(currentUser!!.currentProject)
        myView.team_user_profile.setText(currentUser!!.teamName)

        disableEditing()

        myView.save_button_profile_user_profile.alpha = 0.0F
        myView.cancel_button_profile_user_profile.alpha = 0.0F

        Picasso.get().load(currentUser!!.profileImage!!.convertBitmapToUri(App.instance()!!.applicationContext))
            .into(myView.picture_user_profile)

    }

    fun setListeners(){

        disableEditing()

        var editMode = false

        edit_button_profile_user_profile?.setOnClickListener {
            edit_button_profile_user_profile?.alpha = 0.0F
            username_user_profile?.setFocusableInTouchMode(true)// to enable editing;
            email_user_profile?.setFocusableInTouchMode(true)
            skills_user_profile?.setFocusableInTouchMode(true)
            currentProject_user_profile?.setFocusableInTouchMode(true)
            team_user_profile?.setFocusableInTouchMode(true)

            save_button_profile_user_profile.alpha = 1.0F
            cancel_button_profile_user_profile.alpha = 1.0F

            editMode = true
        }

        picture_user_profile.setOnClickListener{

            if(!editMode) return@setOnClickListener

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUESTCODE_PHOTO)

        }

        save_button_profile_user_profile.setOnClickListener {
            edit_button_profile_user_profile?.alpha = 1.0F
            save_button_profile_user_profile.alpha = 0.0F
            cancel_button_profile_user_profile.alpha = 0.0F
            disableEditing()

            currentUser!!.username = username_user_profile.text.toString()
            currentUser!!.email = email_user_profile.text.toString()
            currentUser!!.position = skills_user_profile.text.toString()
            currentUser!!.teamName = team_user_profile.text.toString()
            currentUser!!.currentProject = currentProject_user_profile.text.toString()

            listenerUserProfile?.updateUser(currentUser!!, pictureIsChanged)

            editMode = false
        }
        cancel_button_profile_user_profile.setOnClickListener {
            edit_button_profile_user_profile?.alpha = 1.0F
            save_button_profile_user_profile.alpha = 0.0F
            cancel_button_profile_user_profile.alpha = 0.0F
            disableEditing()

            editMode = false
        }

        delete_user_profile.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm deletion")
            builder.setMessage("Are you sure you want to permanently delete your EConnect profile?")

            builder.setPositiveButton("YES"){ dialog, which ->
                listenerUserProfile?.deleteUser(currentUser!!)
            }

            builder.setNegativeButton("TAKE ME BACK"){ _,_ ->  }

            val dialog = builder.create()

            dialog.show()
        }

        logout_user_profile.setOnClickListener {
            listenerUserProfile?.logoutUser(currentUser!!)
            HomeActivity.currentFragmet = EmployeesFragment()
        }
    }

    private fun disableEditing(){
        myView?.username_user_profile?.setFocusable(false) // to disable editing
        myView?.email_user_profile?.setFocusable(false)
        myView?.skills_user_profile?.setFocusable(false)
        myView?.currentProject_user_profile?.setFocusable(false)
        myView?.team_user_profile?.setFocusable(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserProfileFragmentInteractionListener) {
            listenerUserProfile = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    private var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUESTCODE_PHOTO && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            currentUser!!.profileImageUrl = selectedPhotoUri.toString()

            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedPhotoUri)
            picture_user_profile_register.setImageBitmap(bitmap)
            photo_button_register.alpha = 0f

            pictureIsChanged = true
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerUserProfile = null
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
    interface OnUserProfileFragmentInteractionListener {
        fun updateUser(user: User, pictureIsChanged: Boolean)
        fun logoutUser(user: User)
        fun deleteUser(user: User)
    }
    companion object {

        const val TAG = "UserProfileFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}

package com.employeeconnect.ui.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.DeleteUserCommand
import com.employeeconnect.domain.commands.LogoutUserCommand
import com.employeeconnect.domain.commands.UpdateUserCommand
import com.employeeconnect.ui.Activities.HomeActivity
import com.employeeconnect.ui.Activities.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_basic_info_register.*
import kotlinx.android.synthetic.main.fragment_basic_info_register.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listenerUserProfile: OnUserProfileFragmentInteractionListener? = null
    private var currentUser: User? = null
    private var pictureIsChanged = false
    private var REQUESTCODE_PHOTO = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = HomeActivity.currentUser

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        view.username_user_profile.setText(currentUser!!.username)
        view.email_user_profile.setText(currentUser!!.email)
        view.skills_user_profile.setText(currentUser!!.position)
        view.currentProject_user_profile.setText(currentUser!!.currentProject)
        view.team_user_profile.setText(currentUser!!.teamName)

        disableEditing()

        view.save_button_profile_user_profile.alpha = 0.0F
        view.cancel_button_profile_user_profile.alpha = 0.0F

        Picasso.get().load(currentUser!!.profileImageUrl).into(view.picture_user_profile)

        return view
    }

    fun disableEditing(){
        view?.username_user_profile?.setFocusable(false) // to disable editing
        view?.email_user_profile?.setFocusable(false)
        view?.skills_user_profile?.setFocusable(false)
        view?.currentProject_user_profile?.setFocusable(false)
        view?.team_user_profile?.setFocusable(false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listenerUserProfile?.onUserProfileFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserProfileFragmentInteractionListener) {
            listenerUserProfile = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onStart() {
        super.onStart()

        disableEditing()
        val picture = view?.picture_user_profile

        picture?.setOnClickListener{
            Log.d("TAG", "oooo")
        }
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

            UpdateUserCommand(currentUser!!, pictureIsChanged){
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
            }.execute()

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

                val intent = Intent(context, LoginActivity::class.java)

                DeleteUserCommand(currentUser!!.uid){
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    Toast.makeText(context, "This user profile is successfully deleted", Toast.LENGTH_SHORT).show()
                }.execute()
            }

            builder.setNegativeButton("TAKE ME BACK"){ _,_ ->  }

            val dialog = builder.create()

            dialog.show()
        }

        logout_user_profile.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)

            LogoutUserCommand{
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.execute()

            HomeActivity.currentFragmet = EmployeesFragment()
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
        // TODO: Update argument type and name
        fun onUserProfileFragmentInteraction(uri: Uri)
    }
    companion object {
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

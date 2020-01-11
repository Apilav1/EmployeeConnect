package com.employeeconnect.ui.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.employeeconnect.extensions.revealAndDropAnimation

import com.employeeconnect.R
import kotlinx.android.synthetic.main.fragment_contact_info_register.*
import kotlinx.android.synthetic.main.fragment_contact_info_register.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactInfoRegisterFragment.OnContactInfoRegisterFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactInfoRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactInfoRegisterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listenerContactInfoRegister: OnContactInfoRegisterFragmentInteractionListener? = null
    private var fragmentContainer: ViewGroup? = null
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
        fragmentContainer = container
        myView = inflater.inflate(R.layout.fragment_contact_info_register, container, false)
        return myView
    }

    fun onButtonPressed(view: View, githubUsername: String,
                                         linkedInUsername: String, skills: String) {
        listenerContactInfoRegister?.onContactInfoRegisterFragmentInteraction(view, githubUsername, linkedInUsername, skills)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentContainer.revealAndDropAnimation(listOf<View>(
            github_username_register,
            linkenin_link_register,
            skills_register,
            back_button_contact_info_register,
            next_button_contact_info_register
        ))

        back_button_contact_info_register.setOnClickListener {
            onButtonPressed(it, "", "", "")
        }

        next_button_contact_info_register.setOnClickListener {

            val githubUsername = github_username_register.text.toString()
            val linkedInUsername = linkenin_link_register.text.toString()
            val skills = skills_register.text.toString()

            onButtonPressed(it, githubUsername, linkedInUsername, skills)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContactInfoRegisterFragmentInteractionListener) {
            listenerContactInfoRegister = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerContactInfoRegister = null
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
    interface OnContactInfoRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onContactInfoRegisterFragmentInteraction(view: View, githubUsername: String,
                                                        linkedInUsername: String, skills: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactInfoRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactInfoRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

package com.employeeconnect.ui.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.employeeconnect.extensions.revealAndDropAnimation

import com.employeeconnect.R
import kotlinx.android.synthetic.main.fragment_employee_info_register.*
import kotlinx.android.synthetic.main.fragment_employee_info_register.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EmployeeInfoRegisterFragment.OnEmployeeInfoRegisterFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EmployeeInfoRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmployeeInfoRegisterFragment : Fragment(){
    private var param1: String? = null
    private var param2: String? = null
    private var listenerEmployeeInfoRegister: OnEmployeeInfoRegisterFragmentInteractionListener? = null
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
        myView =  inflater.inflate(R.layout.fragment_employee_info_register, container, false)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentContainer.revealAndDropAnimation(listOf<View>(
            position_register,
            team_register,
            project_register,
            back_button_employee_info_register,
            complete_registration_button_register
        ))

        back_button_employee_info_register.setOnClickListener {
            onButtonPressed(it, "", "", "")
        }
        complete_registration_button_register.setOnClickListener {

            val position = position_register.text.toString()
            val teamName = team_register.text.toString()
            val currentProject = project_register.text.toString()

//            it.isClickable = false
            progress_register.visibility = View.VISIBLE
            onButtonPressed(it, position, teamName, currentProject)
        }
    }

    public fun hideProgress(){
        progress_register.visibility = View.GONE
    }

    fun onButtonPressed(view: View, position: String, teamName: String, currentProject: String) {
        listenerEmployeeInfoRegister?.onEmployeeInfoRegisterFragmentInteraction(view, position, teamName, currentProject)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEmployeeInfoRegisterFragmentInteractionListener) {
            listenerEmployeeInfoRegister = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerEmployeeInfoRegister = null
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
    interface OnEmployeeInfoRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onEmployeeInfoRegisterFragmentInteraction(view: View, position: String, teamName: String, currentProject: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmployeeInfoRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeeInfoRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

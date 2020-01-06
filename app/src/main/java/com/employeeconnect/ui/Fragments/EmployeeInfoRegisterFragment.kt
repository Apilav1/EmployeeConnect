package com.employeeconnect.ui.Fragments

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
class EmployeeInfoRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listenerEmployeeInfoRegister: OnEmployeeInfoRegisterFragmentInteractionListener? = null
    private var fragmentContainer: ViewGroup? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_info_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gif_imageView_register.alpha = 0.0f

        fragmentContainer.revealAndDropAnimation(listOf<View>(
            position_register,
            team_register,
            back_button_employee_info_register,
            complete_registration_button_register
        ))

        back_button_employee_info_register.setOnClickListener {
            onButtonPressed(it)
        }
        complete_registration_button_register.setOnClickListener {
            if(employeeInfoValidation()){
                it.isClickable = false
                back_button_employee_info_register.isClickable = false
                onButtonPressed(it)

                gif_imageView_register.alpha = 1.0f
                Glide.with(this).load(R.drawable.loading).into(gif_imageView_register);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(view: View) {
        listenerEmployeeInfoRegister?.onEmployeeInfoRegisterFragmentInteraction(view)
    }

    private fun employeeInfoValidation(): Boolean{
        val position = position_register.text.toString()
        val teamName = team_register.text.toString()

        if(position.isEmpty() || teamName.isEmpty()){
            Toast.makeText(context, "Please enter required information", Toast.LENGTH_SHORT).show()
        }

        return true
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
        fun onEmployeeInfoRegisterFragmentInteraction(view: View)
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

package com.employeeconnect.ui.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import com.employeeconnect.R

import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.GetUsersCommand
import com.employeeconnect.domain.commands.MakeUserAModeratorCommand
import com.employeeconnect.domain.commands.VerifyUserCommand
import com.employeeconnect.ui.Activities.ChatLogActivity
import com.employeeconnect.ui.Activities.HomeActivity
import com.employeeconnect.ui.view.UserRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_employees.*
import kotlinx.android.synthetic.main.fragment_employees_list.*
import kotlinx.android.synthetic.main.fragment_employees_list.view.*
import java.lang.Exception

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [EmployeesFragment.OnListEmployeesFragmentInteractionListener] interface.
 */
class EmployeesFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListEmployeesFragmentInteractionListener? = null
    private var users: ArrayList<User>? = null
    private var adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            //users = it.getParcelableArrayList<User>("users")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_employees, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListEmployeesFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onStart() {
        super.onStart()

        getUsers()
    }

    private fun getUsers(){

        adapter = GroupAdapter()
        recycleview_employees.adapter = adapter

        try {
            GetUsersCommand{
                adapter.clear()

                it.forEach { user ->
                    if(user.uid != HomeActivity.currentUserId) {
                        users?.add(user)
                        adapter.add(UserRow(context!!, user))
                    }
                }

                adapter.notifyDataSetChanged()
            }.execute()
        }
        catch (e: Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserRow

            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user)
            startActivity(intent)
        }

        adapter.setOnItemLongClickListener { item, view ->

            if(!HomeActivity.currentUser!!.moderator) return@setOnItemLongClickListener true

            val userItem = item as UserRow
            val user = userItem.user

            if(!user.verified){
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm verification")
                builder.setMessage("Are you sure you want to verify this profile?")

                builder.setPositiveButton("YES"){ dialog, which ->

                    VerifyUserCommand(user.uid){
                        verification_icon_employees_list.alpha = 0.0F
                    }.execute()
                }

                builder.setNegativeButton("TAKE ME BACK"){ _,_ ->  }

                val dialog = builder.create()
                dialog.show()
            }
            else if(!user.moderator){
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm this update")
                builder.setMessage("Are you sure you want to allow this user to be moderator?")

                builder.setPositiveButton("YES"){ dialog, which ->

                    MakeUserAModeratorCommand(user.uid){
                        moderator_imageview_employees_list.alpha = 1.0F
                    }.execute()
                }

                builder.setNegativeButton("TAKE ME BACK"){ _,_ ->  }

                val dialog = builder.create()
                dialog.show()
            }

            true
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListEmployeesFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListEmployeesFragmentInteraction(user: User)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val USER_KEY = "USER_KEY"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            EmployeesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

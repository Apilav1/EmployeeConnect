package com.employeeconnect.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import com.employeeconnect.R

import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.chatLog.ChatLogActivity
import com.employeeconnect.ui.view.UserRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_employees.*
import kotlinx.android.synthetic.main.fragment_employees_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [EmployeesFragment.OnListEmployeesFragmentInteractionListener] interface.
 */
class EmployeesFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListEmployeesFragmentInteractionListener? = null

    private var adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
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

    override fun onResume() {
        super.onResume()

        listener?.requestUsers()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem!!.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.clear()
                val result: ArrayList<User> = ArrayList()

                val filterPattern = newText.toString().toLowerCase().trim()

                if(HomeActivity.currentUsers == null || HomeActivity.currentUsers?.size == 0
                    || HomeActivity.currentUsers?.size == 1) return false

                result.addAll(HomeActivity.currentUsers!!.filter{
                    it.username.toLowerCase().contains(filterPattern)
                })

                result.forEach {
                    if(it.uid != HomeActivity.currentUser?.uid)
                        adapter.add(UserRow(context!!, it))
                }
                adapter.notifyDataSetChanged()

                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
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

        fun onListEmployeesFragmentInteraction(user: User)
        fun verifyUser(userId: String)
        fun makeUserAModerator(userId: String)
        fun requestUsers()

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val USER_KEY = "USER_KEY"
        const val TAG = "EmployeesFragment"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            EmployeesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

     fun showUsers(users: ArrayList<User>) {

         if(users.size == 0 || recycleview_employees == null) return

         adapter.clear()
         adapter = GroupAdapter()
         recycleview_employees.adapter = adapter

        users.forEach { user ->
            if(user.uid != HomeActivity.currentUser!!.uid) {
                adapter.add(UserRow(context!!, user))
            }
        }

        adapter.notifyDataSetChanged()

         adapter.setOnItemClickListener { item, view ->
             val userItem = item as UserRow

             val intent = Intent(view.context, ChatLogActivity::class.java)
             //intent.putExtra(USER_KEY, userItem.user) TransactionTooLargeException: data parcel size 805756 bytes
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
                     listener?.verifyUser(user.uid)
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
                     listener?.makeUserAModerator(user.uid)
                 }

                 builder.setNegativeButton("TAKE ME BACK"){ _,_ ->  }

                 val dialog = builder.create()
                 dialog.show()
             }

             true
         }
    }

    fun onVerificationSuccess(){
        verification_icon_employees_list.alpha = 0.0F
    }

    fun onMakingUserAModeratorSuccess(){
        moderator_imageview_employees_list.alpha = 1.0F
    }
}

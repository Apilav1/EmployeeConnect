package com.employeeconnect.ui.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.GetLatestMessagesCommand
import com.employeeconnect.domain.commands.GetMultipleUsersByIdCommand
import com.employeeconnect.ui.Activities.ChatLogActivity
import com.employeeconnect.ui.view.LatestMessageRow
import com.employeeconnect.ui.view.UserRow

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_messages.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [LatestMessagesFragment.OnMessagesListFragmentInteractionListener] interface.
 */
class LatestMessagesFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var adapter = GroupAdapter<GroupieViewHolder>()

    private var listener: OnMessagesListFragmentInteractionListener? = null
    private var latestMessagesWithoutUsers: ArrayList<Message>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onStart() {
        super.onStart()

        adapter = GroupAdapter()
        recycleview_messages.adapter = adapter
        recycleview_messages.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        getLatestMessages()
    }

    private fun getLatestMessages(){

        GetLatestMessagesCommand{ messages ->
            if(messages.size == 0) return@GetLatestMessagesCommand

            latestMessagesWithoutUsers = messages
            val toUserIds: ArrayList<String> = ArrayList()

            latestMessagesWithoutUsers!!.sortBy { it.timeStamp }
            latestMessagesWithoutUsers!!.forEach {
                toUserIds.add(it.toUser)
            }

            GetMultipleUsersByIdCommand(toUserIds){ users ->

                adapter.clear()

                latestMessagesWithoutUsers!!.forEach { message ->
                    users.forEach{user ->
                        if(user.uid == message.toUser){
                            adapter.add(LatestMessageRow(user, message))
                        }
                    }
                }

            }.execute()

        }.execute()

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as LatestMessageRow
            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra(EmployeesFragment.USER_KEY, userItem.toUser)
            startActivity(intent)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessagesListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
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
    interface OnMessagesListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListMessagesFragmentInteraction(user: User)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            LatestMessagesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

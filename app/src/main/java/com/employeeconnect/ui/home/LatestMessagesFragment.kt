package com.employeeconnect.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.view.LatestMessageRow

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_messages.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [LatestMessagesFragment.OnMessagesListFragmentInteractionListener] interface.
 */
class LatestMessagesFragment : Fragment() {

    private var adapter = GroupAdapter<GroupieViewHolder>()
    private var currentUser: User? = null

    private var listener: OnMessagesListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.currentUser = HomeActivity.currentUser

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

    }

    override fun onResume() {
        super.onResume()

        listener?.requestLatestMessages()
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
        fun requestLatestMessages()
        fun updateMessageSeenField(message: Message)
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

    fun showLatestMessages(messages: HashMap<User, Message>?) {

        if(messages?.size == 0 || recycleview_messages == null) return

        adapter.clear()
        adapter = GroupAdapter()
        recycleview_messages.adapter = adapter

        for((key, value) in messages!!){
            adapter.add(LatestMessageRow(key, value))
        }
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as LatestMessageRow

            userItem.message.seen = true

            listener?.updateMessageSeenField(userItem.message)

//            val intent = Intent(view.context, ChatLogActivity::class.java)
//            intent.putExtra(EmployeesFragment.USER_KEY, userItem.toUser)
            listener?.onListMessagesFragmentInteraction(userItem.toUser)

            HomeActivity.currentFragmet = LatestMessagesFragment()
        }
    }
}

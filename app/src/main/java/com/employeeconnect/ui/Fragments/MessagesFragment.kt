package com.employeeconnect.ui.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.employeeconnect.ui.Adapters.MyMessageRecyclerViewAdapter
import com.employeeconnect.R

import com.employeeconnect.ui.Fragments.dummy.DummyContent
import com.employeeconnect.ui.Fragments.dummy.DummyContent.DummyItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_messages.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MessagesFragment.OnMessagesListFragmentInteractionListener] interface.
 */
class MessagesFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var adapter = GroupAdapter<GroupieViewHolder>()

    private var listener: OnMessagesListFragmentInteractionListener? = null

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

        recycleview_messages.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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
        fun onListMessagesFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MessagesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

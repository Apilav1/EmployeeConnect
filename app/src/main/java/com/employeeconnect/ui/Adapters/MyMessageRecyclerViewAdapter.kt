package com.employeeconnect.ui.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.employeeconnect.R


import com.employeeconnect.ui.Fragments.LatestMessagesFragment.OnMessagesListFragmentInteractionListener
import com.employeeconnect.ui.Fragments.dummy.DummyContent.DummyItem
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_messages_row.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMessageRecyclerViewAdapter(
    private val mValues: List<DummyItem>,
    private val mListener: OnMessagesListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
           // mListener?.onListMessagesFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_messages_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        holder.mIdView.text = item.id
//        holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

        val userPhoto = holder.itemView.imageView_fragment_messages
        Picasso.get().load(R.drawable.andreybreslav).into(userPhoto)

        var message: String = "hey what's up, welcome to the team! " +
                "what r u doing all this time i want to see you" +
                "even though i kinda dont now you more than just a friend" +
                "i really kinda miss you"

        holder.itemView.username_fragment_messages.setText("Andrey Breslav")

        val min = 55
        if(message.length > min){
            message = message.subSequence(0, min).toString() + "..."
        }
        holder.itemView.messagetext_fragment_messages.setText(message)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        val mIdView: TextView = mView.item_number
//        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" //+ mContentView.text + "'"
        }
    }
}

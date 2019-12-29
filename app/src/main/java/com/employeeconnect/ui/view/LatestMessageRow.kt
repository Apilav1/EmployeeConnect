package com.employeeconnect.ui.view

import android.util.Log
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.extensions.getDateTimeFromTimestamp
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_messages_row.view.*

class LatestMessageRow(val toUser: User, val message: Message): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val currentUserSentMessage = toUser.uid == message.toUser

        viewHolder.itemView.username_fragment_messages.setText(toUser.username)
        Picasso.get().load(toUser.profileImageUrl).into(viewHolder.itemView.imageView_fragment_messages)

        if(currentUserSentMessage)
            viewHolder.itemView.messagetext_fragment_messages.text = "You: ${message.text}"
        else
            viewHolder.itemView.messagetext_fragment_messages.text = message.text

        val messageDate = message.timeStamp.toString()

        viewHolder.itemView.messageDate_fragment_message_row.text = messageDate.getDateTimeFromTimestamp()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_messages_row
    }
}
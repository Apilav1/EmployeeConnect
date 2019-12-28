package com.employeeconnect.ui.view

import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.extensions.getDateTimeFromTimestamp
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_messages_row.view.*

class LatestMessageRow(val user: User, val message: Message): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_fragment_messages.setText(user.username)
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_fragment_messages)

        viewHolder.itemView.messagetext_fragment_messages.text = message.text

        val messageDate = message.timeStamp.toString()
        messageDate.getDateTimeFromTimestamp()

        viewHolder.itemView.messageDate_fragment_message_row.text = messageDate
    }

    override fun getLayout(): Int {
        return R.layout.fragment_messages_row
    }
}
package com.employeeconnect.ui.view

import android.graphics.Typeface
import android.util.Log
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.extensions.convertBitmapToUri
import com.employeeconnect.extensions.getDateTimeFromTimestamp
import com.employeeconnect.ui.App
import com.employeeconnect.ui.home.HomeActivity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_messages_row.view.*
import org.jetbrains.anko.custom.style

class LatestMessageRow(val toUser: User, val message: Message): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val currentUserSentMessage = HomeActivity.currentUser!!.uid == message.fromUser

        viewHolder.itemView.username_fragment_messages.setText(toUser.username)
        Picasso.get().load(toUser.profileImage!!.convertBitmapToUri(App.instance()!!.applicationContext)).into(viewHolder.itemView.imageView_fragment_messages)

        if(currentUserSentMessage)
            viewHolder.itemView.messagetext_fragment_messages.text = "You: ${message.text}"
        else
            viewHolder.itemView.messagetext_fragment_messages.text = message.text

        if(!message.seen && message.fromUser == toUser.uid){
            viewHolder.itemView.messagetext_fragment_messages.setTypeface(null, Typeface.BOLD)
        }

        val messageDate = message.timeStamp.toString()

        viewHolder.itemView.messageDate_fragment_message_row.text = messageDate.getDateTimeFromTimestamp()

    }

    override fun getLayout(): Int {
        return R.layout.fragment_messages_row
    }
}
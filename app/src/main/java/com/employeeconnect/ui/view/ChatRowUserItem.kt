package com.employeeconnect.ui.view

import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_messages_row.view.*

class ChatRowUserItem(val user: User): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_fragment_messages.setText(user.username)
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_fragment_messages)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_messages_row
    }
}
package com.employeeconnect.ui.view

import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_left_side_row.view.*
import kotlinx.android.synthetic.main.chat_right_side_row.view.*

class ChatFromCurrentUserItem(val text: String): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.chat_right_side_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_right_side_row.text = text
    }
}

class ChatToUserItem(val text: String, val user: User): Item<GroupieViewHolder>(){

    override fun getLayout(): Int {
        return R.layout.chat_left_side_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_left_side_row.text = text

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView_left_side_row
        Picasso.get().load(uri).into(targetImageView)
    }
}
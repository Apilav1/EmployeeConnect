package com.employeeconnect.ui.view

import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_employees_list.view.*

class UserRow (val user: User) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.fragment_employees_list
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val view = viewHolder.itemView
        view.username_fragment_employees_list.text = user.username
        view.position_fragment_employees_list.text = user.position
        view.github_textview_fragment_employees_list.text = user.githubUsername
        view.linkedin_textview_fragment_employees_list.text = user.linkedInUsername

        val targetImageView = view.imageview_fragment_employees_list
        Picasso.get().load(user.profileImageUrl).into(targetImageView)

        val logo = view.github_logo_imageview
        Picasso.get().load(R.drawable.icon).into(logo)

        val linkeInlogo = view.linkedin_logo_imageview
        Picasso.get().load(R.drawable.linkedin_logo_mdpi).into(linkeInlogo)

        val arrow = view.arrow_fragment_employees_list
        Picasso.get().load(R.drawable.arrow_icon).into(arrow)
    }


}
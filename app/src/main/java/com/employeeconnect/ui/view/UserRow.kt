package com.employeeconnect.ui.view

import android.content.Context
import com.employeeconnect.R
import com.employeeconnect.domain.Models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_employees_list.view.*

class UserRow (val context: Context, val user: User) : Item<GroupieViewHolder>() {

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
        Picasso.get().load(R.drawable.linkedin_icon2).into(linkeInlogo)

        val arrow = view.arrow_fragment_employees_list
        Picasso.get().load(R.drawable.arrow_icon).into(arrow)

        val wrenchIcon = view.moderator_imageview_employees_list

        Picasso.get().load(R.drawable.wrench_icon_48).into(wrenchIcon)

        if(user.moderator){
            wrenchIcon.alpha = 1.0F
        }
        else
        {
            wrenchIcon.alpha = 0.0F
        }

        val validationIcon = view.verification_icon_employees_list

        if(!user.verified){
            validationIcon.alpha = 1.0F
            Picasso.get().load(R.drawable.exclamation_mark_icon).into(validationIcon)
        }
        else{
            validationIcon.alpha = 0.0F
        }
    }


}
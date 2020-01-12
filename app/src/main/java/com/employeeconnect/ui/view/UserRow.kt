package com.employeeconnect.ui.view

import android.content.Context
import android.view.View
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
        logo.setBackgroundResource(R.drawable.icon)
        //Picasso.get().load(R.drawable.icon).into(logo)

        val linkeInlogo = view.linkedin_logo_imageview
        linkeInlogo.setBackgroundResource(R.drawable.linkedin_icon2)

        val arrow = view.arrow_fragment_employees_list
        arrow.setBackgroundResource(R.drawable.arrow_icon)

        val wrenchIcon = view.moderator_imageview_employees_list

        wrenchIcon.setBackgroundResource(R.drawable.wrench_icon_48)

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
            validationIcon.setBackgroundResource(R.drawable.exc_mark2)
        }
        else{
            validationIcon.alpha = 0.0F
        }
    }

}
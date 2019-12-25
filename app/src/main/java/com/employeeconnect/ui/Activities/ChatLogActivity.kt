package com.employeeconnect.ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.employeeconnect.R
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.AddChatRoomIdToUsersCommand
import com.employeeconnect.domain.commands.CreateChatRoomCommand
import com.employeeconnect.domain.commands.SendMessageCommand
import com.employeeconnect.domain.commands.SetMessagesListenerCommand
import com.employeeconnect.ui.Fragments.EmployeesFragment
import com.employeeconnect.ui.view.ChatFromCurrentUserItem
import com.employeeconnect.ui.view.ChatToUserItem
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser: User? = null
    var currentUser: User? = null
    var currentCharRoomId: String? = null
    var messageListenerSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra(EmployeesFragment.USER_KEY)
        currentUser = HomeActivity.currentUser

        recycleview_chat_log.adapter = adapter

        /*
         * new user
         */
        if(currentUser?.chatRooms?.size == 0){
            Log.d(TAG, "nema soba")
            createRoom()
        }
        else{
            Log.d(TAG, "ima soba")
            /**
             * find the correct room
             */
            var charRoomAlreadyExists: Boolean = false
            for ((key, value) in currentUser!!.chatRooms){
                Log.d("CHATTT", key+" "+value)
                if(value == toUser!!.uid) {
                    currentCharRoomId = key
                    charRoomAlreadyExists = true
                    setMessageListener()
                    break
                }
            }
            if(!charRoomAlreadyExists){
                //createRoom()
            }

        }
        //TODO: u createRoom or FindCorrectRoom ide listener
        sendbutton_chat_log.setOnClickListener {

            val message = Message(currentUser!!.uid, toUser!!.uid, edittext_chat_log.text.toString(), System.currentTimeMillis() / 1000)
            SendMessageCommand(currentCharRoomId!!, message).execute()

            //adapter.add(ChatFromCurrentUserItem(edittext_chat_log.text.toString()))

            edittext_chat_log.text.clear()
            recycleview_chat_log.scrollToPosition(adapter.itemCount - 1)

            if(!messageListenerSet){
                messageListenerSet = true
                setMessageListener()
            }
            //TODO: latest messages
        }
    }

    private fun setMessageListener(){

        SetMessagesListenerCommand(currentCharRoomId!!){ messages->
            adapter.clear()

            messages.forEach {
                if(it.fromUser == currentUser!!.uid){
                    adapter.add(ChatFromCurrentUserItem(it.text))
                }
                else{
                    adapter.add(ChatToUserItem(it.text, toUser!!))
                }
            }
        }.execute()

    }

    private fun createRoom(){
        CreateChatRoomCommand(arrayListOf(currentUser!!.uid, toUser!!.uid)){
            //currentUser!!.chatRooms.add(it)
            currentCharRoomId = it

            AddChatRoomIdToUsersCommand(arrayListOf(currentUser!!.uid, toUser!!.uid), currentCharRoomId!!).execute()
            Log.d(TAG, "dobili id $it")
        }.execute()
    }

    companion object{
        val TAG = "CHATTT"
    }
}

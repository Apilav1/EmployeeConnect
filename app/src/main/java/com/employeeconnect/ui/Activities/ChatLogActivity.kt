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
        Log.d("CHATTT", "to user ${toUser!!.uid}")
        currentUser = HomeActivity.currentUser

        recycleview_chat_log.adapter = adapter

        dealWithChatRooms()

       sendbutton_chat_log.setOnClickListener {
                performSendMessage()
        }
    }

    private fun performSendMessage(){

        val message = Message(currentUser!!.uid, toUser!!.uid, edittext_chat_log.text.toString(), System.currentTimeMillis() / 1000)
        SendMessageCommand(currentCharRoomId!!, message).execute()

        edittext_chat_log.text.clear()
        recycleview_chat_log.scrollToPosition(adapter.itemCount - 1)

        if(!messageListenerSet){
            messageListenerSet = true
            setMessageListener()
        }
        //TODO: latest messages
    }

    private fun dealWithChatRooms(){
        val currentUserHaveNoRooms = currentUser?.chatRooms?.size == 0

        if(currentUserHaveNoRooms){
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
                if(value == toUser!!.uid) {
                    Log.d("CHATTT", "key: "+ key+" value: "+value)
                    currentCharRoomId = key
                    charRoomAlreadyExists = true
                    setMessageListener()
                    break
                }
            }
            if(!charRoomAlreadyExists){
                Log.d("CHATTT", "NEMA")
                createRoom()
            }

        }
        //TODO: u createRoom or FindCorrectRoom ide listener
    }

    private fun setMessageListener(){

        SetMessagesListenerCommand(currentCharRoomId!!){ messages->
            adapter.clear()

            //Because firebase firestore add documents in random order
            messages.sortBy { it.timeStamp }

            messages.forEach{
                if(it.fromUser == currentUser!!.uid){
                    adapter.add(ChatFromCurrentUserItem(it.text))
                }
                else{
                    adapter.add(ChatToUserItem(it.text, toUser!!))
                }
            }
            //TODO: latest message right here
        }.execute()

    }

    private fun createRoom(){
        CreateChatRoomCommand(arrayListOf(currentUser!!.uid, toUser!!.uid)){
            //currentUser!!.chatRooms.add(it)
            currentCharRoomId = it

            window.setBackgroundDrawableResource(R.drawable.lightblueandwhitebackground22)

            currentUser!!.chatRooms.put(currentCharRoomId!!, toUser!!.uid)
            toUser!!.chatRooms.put(currentCharRoomId!!, currentUser!!.uid)

            AddChatRoomIdToUsersCommand(arrayListOf(currentUser!!, toUser!!), currentCharRoomId!!).execute()

            Log.d(TAG, "dobili id $it")

            if(!messageListenerSet){
                messageListenerSet = true
                setMessageListener()
            }
        }.execute()
    }

    companion object{
        val TAG = "CHATTT"
    }
}

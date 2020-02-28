package com.employeeconnect.ui.chatLog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.ui.home.EmployeesFragment
import com.employeeconnect.ui.home.HomeActivity
import com.employeeconnect.ui.view.ChatFromCurrentUserItem
import com.employeeconnect.ui.view.ChatToUserItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), ChatLogView {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var toUser: User? = null
    private var currentUser: User? = null
    private var currentCharRoomId: String? = null
    private var messageListenerSet: Boolean = false
    private val presenter: ChatLogPresenter = ChatLogPresenter(this, ChatLogInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //toUser = intent.getParcelableExtra(EmployeesFragment.USER_KEY) TransactionTooLargeException: data parcel size 805756 bytes
        toUser = HomeActivity.toUser

        currentUser = HomeActivity.currentUser

        recycleview_chat_log.adapter = adapter

        dealWithChatRooms()

       sendbutton_chat_log.setOnClickListener {
                performSendMessage()
        }
    }

    private fun performSendMessage(){

        val message = Message(currentUser!!.uid, toUser!!.uid, edittext_chat_log.text.toString(),
                                System.currentTimeMillis() / 1000, currentCharRoomId!!, false)

        edittext_chat_log.text.clear()

        presenter.sendMessage(currentCharRoomId!!, message)
    }

    private fun dealWithChatRooms(){
        val currentUserHaveNoRooms = currentUser?.chatRooms?.size == 0

        if(currentUserHaveNoRooms){
            createRoom()
        }
        else{
            /**
             * find the correct room
             */
            var charRoomAlreadyExists = false
            for ((key, value) in currentUser!!.chatRooms){
                if(value == toUser!!.uid) {
                    currentCharRoomId = key
                    charRoomAlreadyExists = true
                    presenter.setChatRoomListener(currentCharRoomId!!)
                    break
                }
            }
            if(!charRoomAlreadyExists){
                createRoom()
            }

        }
    }

    private fun createRoom(){
        presenter.createChatRoom(arrayListOf(currentUser!!, toUser!!))
    }

    override fun onMessageSendSuccessfully() {

        recycleview_chat_log.scrollToPosition(adapter.itemCount - 1)

        if(!messageListenerSet){
            messageListenerSet = true
            presenter.setChatRoomListener(currentCharRoomId!!)
        }
    }

    override fun onChatRoomCreated(id: String) {
        currentCharRoomId = id

        currentUser!!.chatRooms[currentCharRoomId!!] = toUser!!.uid
        toUser!!.chatRooms[currentCharRoomId!!] = currentUser!!.uid

        presenter.updateUsersChatRooms(arrayListOf(currentUser!!, toUser!!), currentCharRoomId!!)

    }

    override fun onUpdateUsersWithChatRooms() {
        if(!messageListenerSet){
            messageListenerSet = true
            presenter.setChatRoomListener(currentCharRoomId!!)
        }
    }

    override fun onChatRoomListener(messages: ArrayList<Message>) {
        adapter.clear()

        messages.forEach{
                if(it.fromUser == currentUser!!.uid){
                        adapter.add(ChatFromCurrentUserItem(it.text))
                }
                else{
                        adapter.add(ChatToUserItem(it.text, toUser!!))
                }
            }
    }

    override fun showError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    companion object{
        val TAG = "ChatLogActivityy"
    }
}

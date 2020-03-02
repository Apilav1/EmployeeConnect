package com.employeeconnect.data.db

import android.util.Log
import com.employeeconnect.extensions.*
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.db.User as DbUser
import com.employeeconnect.domain.datasource.DataSource
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class EmployeeConnectDb (private val employeeConnectDbHelper: EmployeeConnectDbHelper = EmployeeConnectDbHelper.instance,
                         private val dataMapper: DbDataMapper = DbDataMapper()
): DataSource {

    override fun registerNewUser(user: DomainUser, password: String, onSuccess: () -> Unit) {
        //this action is currently not supported by db
}

      fun saveUsers(users: ArrayList<DomainUser>) = employeeConnectDbHelper.use {

        clear(EmployeeTable.NAME)
        clear(ChatRoomTable.NAME)
        clear(EmployeeChatRoomTable.NAME)

        with(dataMapper.convertUsersToDbModel(users)){

            this.forEach {
                //inserting employee info
                insert(EmployeeTable.NAME, *it.map.toVarargArray())
                //inserting employee-chatRoom info & chatRoom ids
                for((key, value) in it.chatRooms){

                    insert(EmployeeChatRoomTable.NAME,
                        EmployeeChatRoomTable.EMPLOYEE_ID to it.uid,
                                EmployeeChatRoomTable.CHATROOM_ID to key,
                                EmployeeChatRoomTable.TO_USER_ID to value)

                    insert(ChatRoomTable.NAME, ChatRoomTable.ID to key)


                }

            }
        }
    }

    fun saveMessages(messages: ArrayList<Message>) = employeeConnectDbHelper.use {

        clear(MessagesTable.NAME)
        clear(ChatRoomMessageTable.NAME)


        with(dataMapper.convertMessagesToDbMessage(messages)){

            var rowCounter = 1

            this.forEach {

                it.uid = insert(MessagesTable.NAME, *it.map.toVarargArray()).toString()

                update(MessagesTable.NAME, MessagesTable.ID to it.uid)
                    .whereSimple("ROWID = ?", rowCounter++.toString())
                    .exec()

                insert(ChatRoomMessageTable.NAME, ChatRoomMessageTable.MESSAGE_ID to it.uid,
                            ChatRoomMessageTable.CHATROOM_ID to it.chatRoomId)

            }
        }

    }

    fun saveLatestMessages(messages: ArrayList<Message>) = employeeConnectDbHelper.use {

        clear(LatestMessagesTable.NAME)

        with(dataMapper.convertMessagesToDbMessage(messages)){

            var rowCounter = 1

            this.forEach {

                it.uid = insert(LatestMessagesTable.NAME, *it.map.toVarargArray()).toString()

                update(LatestMessagesTable.NAME, MessagesTable.ID to it.uid)
                    .whereSimple("ROWID = ?", rowCounter++.toString())
                    .exec()

            }
        }

    }


    override fun getUsers(callback: (ArrayList<DomainUser>) -> Unit)  = employeeConnectDbHelper.use {

        val result = this.select(EmployeeTable.NAME).parseList { DbUser(HashMap(it)) }

        val getChatRoomsRequest = "${EmployeeChatRoomTable.EMPLOYEE_ID} = ?"

        result.forEach {

            it.chatRooms = HashMap()

            val parser = rowParser { chatRoomId: String, toUserId: String ->
                it.chatRooms[chatRoomId] = toUserId
            }

            this.select(EmployeeChatRoomTable.NAME, EmployeeChatRoomTable.CHATROOM_ID, EmployeeChatRoomTable.TO_USER_ID)
                .whereSimple(getChatRoomsRequest, it.uid)
                .parseList {
                    //parser(it)
                }
        }
    }


    override fun getCurrentUserId(callback: (uid: String?) -> Unit) {
        
    }

    override fun fetchCurrentUser(callback: (user: DomainUser) -> Unit) {
        
    }

    override fun sendMessage(chatRoomId: String, message: Message, callback: () -> Unit) {
        //this action is currently not supported by db
} 

    override fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {
        //this action is currently not supported by db
}

    override fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit) {
        //this action is currently not supported by db
}

    override fun addChatRoomIdToUsers(
        users: ArrayList<DomainUser>,
        chatRoomId: String,
        onSuccess: () -> Unit
    ) {
        //this action is currently not supported by db
}

    override fun getLatestMessages(
        chatRooms: ArrayList<String>,
        callback: (ArrayList<Message>) -> Unit
    ) {
        
    }

    override fun updateLatestMessages(message: Message, callback: () -> Unit) {
        //this action is currently not supported by db
}

    override fun getUserById(userId: String, callback: (DomainUser) -> Unit) {
    }

    override fun getMultipleUsersById(
        usersIds: ArrayList<String>,
        callback: (ArrayList<DomainUser>) -> Unit
    ) {
    }

    override fun updateUser(user: DomainUser, pictureChanged: Boolean, callback: () -> Unit) {
        //this action is currently not supported by db
}

    override fun deleteUser(userId: String, callback: () -> Unit) {
        //this action is currently not supported by db
}

    override fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (signInSuccessful: Boolean) -> Unit
    ) {
        //this action is currently not supported by db
}

    override fun logoutUser(callback: () -> Unit) {
        //this action is currently not supported by db
}

    override fun verifyUserProfile(userId: String, callback: () -> Unit) {
        
    }

    override fun makeUserAModerator(userId: String, callback: () -> Unit) {
        
    }

    override fun checkIfUserIsVerified(
        email: String,
        callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit
    ) {
        
    }
}
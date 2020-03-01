package com.employeeconnect.data.db

import android.util.Log
import com.employeeconnect.extensions.*
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.db.User as DbUser
import com.employeeconnect.domain.datasource.DataSource
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

class EmployeeConnectDb (private val employeeConnectDbHelper: EmployeeConnectDbHelper = EmployeeConnectDbHelper.instance,
                         private val dataMapper: DbDataMapper = DbDataMapper()
): DataSource {

    override fun registerNewUser(user: DomainUser, password: String, onSuccess: () -> Unit) {
        //Db should not create a new User
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
                for((_, value) in it.chatRooms){

                    insert(EmployeeChatRoomTable.NAME,
                        EmployeeChatRoomTable.EMPLOYEE_ID to it.uid,
                                EmployeeChatRoomTable.CHATROOM_ID to value)

                    insert(ChatRoomTable.NAME, ChatRoomTable.ID to value)


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
    }


    override fun getCurrentUserId(callback: (uid: String?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchCurrentUser(callback: (user: DomainUser) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessage(chatRoomId: String, message: Message, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addChatRoomIdToUsers(
        users: ArrayList<DomainUser>,
        chatRoomId: String,
        onSuccess: () -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLatestMessages(
        chatRooms: ArrayList<String>,
        callback: (ArrayList<Message>) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLatestMessages(message: Message, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserById(userId: String, callback: (DomainUser) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMultipleUsersById(
        usersIds: ArrayList<String>,
        callback: (ArrayList<DomainUser>) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUser(user: DomainUser, pictureChanged: Boolean, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUser(userId: String, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (signInSuccessful: Boolean) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logoutUser(callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyUserProfile(userId: String, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeUserAModerator(userId: String, callback: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkIfUserIsVerified(
        email: String,
        callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
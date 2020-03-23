package com.employeeconnect.data.db

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.employeeconnect.extensions.*
import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.data.db.User as DbUser
import com.employeeconnect.domain.datasource.DataSource
import com.employeeconnect.ui.App
import com.employeeconnect.ui.activities.BaseActivity
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import com.employeeconnect.data.db.Message as DbMessage
import com.employeeconnect.domain.Models.Message as DomainMessage

class EmployeeConnectDb (private val employeeConnectDbHelper: EmployeeConnectDbHelper = EmployeeConnectDbHelper.instance,
                         private val dataMapper: DbDataMapper = DbDataMapper()
): DataSource {

    companion object {
        val instance by lazy { EmployeeConnectDb() }
    }

    private var currentUserId: String? = null

    override var ready: Boolean = false

    override var preferred: Boolean = true

    private lateinit var broadcastReceiver: BroadcastReceiver

    init {


        employeeConnectDbHelper.use {
            getCurrentUserId {
                if(it != null){
                    ready = true
                    currentUserId = it
                }
            }
        }

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    BaseActivity.BROADCAST_NETWORK_CHANGED -> {
                        val isConnected = intent.getBooleanExtra(BaseActivity.ISCONNECTED, false)

                        preferred = !isConnected

                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(App.instance!!.applicationContext).
            registerReceiver(broadcastReceiver, IntentFilter(BaseActivity.BROADCAST_NETWORK_CHANGED))
    }

    override fun registerNewUser(user: DomainUser, password: String, onSuccess: () -> Unit) {
        //this action is currently not supported by db
    }

      fun saveUsers(users: ArrayList<DomainUser>) = employeeConnectDbHelper.use {


        if (users.size == 0) return@use

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

        if(messages.size == 0) return@use

        clear(MessagesTable.NAME)

        with(dataMapper.convertMessagesToDbMessage(messages)){

            var rowCounter = 1

            this.forEach {

                it.uid = insert(MessagesTable.NAME, *it.map.toVarargArray()).toString()

                update(MessagesTable.NAME, MessagesTable.ID to it.uid)
                    .whereSimple("ROWID = ?", rowCounter++.toString())
                    .exec()

            }
        }

    }

    fun saveLatestMessages(messages: ArrayList<Message>) = employeeConnectDbHelper.use {

        if (messages.size == 0) return@use

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

        val result = this.select(EmployeeTable.NAME).parseList {

            var res = it.toMutableMap()
            res[EmployeeTable.PROFILE_IMAGE] = (res[EmployeeTable.PROFILE_IMAGE] as ByteArray).getImage()

            res[EmployeeTable.MODERATOR] = (res[EmployeeTable.MODERATOR] as Long).toBoolean()
            res[EmployeeTable.VERIFIED] = (res[EmployeeTable.VERIFIED] as Long).toBoolean()

            DbUser(HashMap(res))

        }

        val getChatRoomsRequest = "${EmployeeChatRoomTable.EMPLOYEE_ID} = ?"

        result.forEach { user ->

            user.chatRooms = HashMap()


            this.select(EmployeeChatRoomTable.NAME, EmployeeChatRoomTable.CHATROOM_ID, EmployeeChatRoomTable.TO_USER_ID)
                .whereSimple(getChatRoomsRequest, user.uid)
                .parseList {

                    for((key, value) in it){
                        if(key == EmployeeChatRoomTable.CHATROOM_ID && value != user.uid)
                            user.chatRooms[value.toString()] = it[EmployeeChatRoomTable.TO_USER_ID].toString()
                    }

                }
        }

        callback(dataMapper.convertToDomain(result as ArrayList<DbUser>))
    }

    fun saveCurrentUserId(id: String?) = employeeConnectDbHelper.use {

        if(id == null) return@use

        clear(AppInfo.NAME)

        this.insert(AppInfo.NAME, AppInfo.CURRENT_USER_ID to id)

    }


    override fun getCurrentUserId(callback: (uid: String?) -> Unit): Unit = employeeConnectDbHelper.use{

         this.select(AppInfo.NAME, AppInfo.CURRENT_USER_ID)
             .limit(1)
             .parseOpt {
                 currentUserId = it[AppInfo.CURRENT_USER_ID].toString()
                 callback(it[AppInfo.CURRENT_USER_ID].toString())
             }
        
    }

    override fun fetchCurrentUser(callback: (user: DomainUser) -> Unit) = employeeConnectDbHelper.use{

        val request = "${EmployeeTable.ID} = ?"

        if(currentUserId != null) {

            val result = this.select(EmployeeTable.NAME)
                .whereSimple(request, currentUserId!!)
                .parseOpt{
                    var res = it.toMutableMap()
                    res[EmployeeTable.PROFILE_IMAGE] = (res[EmployeeTable.PROFILE_IMAGE] as ByteArray).getImage()

                    res[EmployeeTable.MODERATOR] = (res[EmployeeTable.MODERATOR] as Long).toBoolean()
                    res[EmployeeTable.VERIFIED] = (res[EmployeeTable.VERIFIED] as Long).toBoolean()

                    DbUser(HashMap(res))
                }
                ?: return@use

            val getChatRoomsRequest = "${EmployeeChatRoomTable.EMPLOYEE_ID} = ?"

            result.chatRooms = HashMap()

            this.select(EmployeeChatRoomTable.NAME, EmployeeChatRoomTable.CHATROOM_ID, EmployeeChatRoomTable.TO_USER_ID)
                .whereSimple(getChatRoomsRequest, result.uid)
                .parseList {

                    for((key, value) in it){
                        if(key == EmployeeChatRoomTable.CHATROOM_ID && value != result.uid)
                            result.chatRooms[value.toString()] = it[EmployeeChatRoomTable.TO_USER_ID].toString()
                    }

                }


            callback(dataMapper.convertUserToDomain(result))
        }


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
    ) = employeeConnectDbHelper.use {

        val request = "${MessagesTable.CHATROOM_ID} = ?"

        val result = ArrayList<DbMessage>()

        chatRooms.forEach { chatRoomId ->

            val m = this.select(LatestMessagesTable.NAME)
                    .whereSimple(request, chatRoomId)
                    .parseOpt {

                        val res = it.toMutableMap()

                        res[LatestMessagesTable.SEEN] = (res[LatestMessagesTable.SEEN] as Long).toBoolean()

                        DbMessage(HashMap(res))
                    }

            if(m != null) result.add(m)

        }

        callback(dataMapper.convertMessagesToDomain(result))

    }

    override fun updateLatestMessages(message: Message, callback: () -> Unit) {
        //this action is currently not supported by db
}

    override fun getUserById(userId: String, callback: (DomainUser) -> Unit) {
        //this action is currently not supported by db
    }

    override fun getMultipleUsersById(
        usersIds: ArrayList<String>,
        callback: (ArrayList<DomainUser>) -> Unit
    )  {
        //this action is not supported by db
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
        //this action is currently not supported by db
    }

    override fun makeUserAModerator(userId: String, callback: () -> Unit) {
        //this action is currently not supported by db
    }

    override fun checkIfUserIsVerified(
        email: String,
        callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit
    ) {
        //this action is currently not supported by db
    }
}
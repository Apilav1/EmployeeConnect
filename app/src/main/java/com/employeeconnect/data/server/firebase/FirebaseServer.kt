package com.employeeconnect.data.server.firebase


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.employeeconnect.data.db.EmployeeConnectDb
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.domain.Models.Message as DomainMessage
import com.employeeconnect.domain.Models.ChatRoom as DomainChatRoom

import com.employeeconnect.domain.datasource.DataSource
import com.employeeconnect.networks.ConnectivityReceiver
import com.employeeconnect.ui.App
import com.employeeconnect.ui.activities.BaseActivity
import kotlin.collections.ArrayList

class FirebaseServer( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                      private val db: EmployeeConnectDb = EmployeeConnectDb.instance
) : DataSource {

    companion object{
         const val TAG = "FirebaseServerTag"
    }

    override var ready: Boolean = false

    override var preferred: Boolean = false

    var broadcastReceiver: BroadcastReceiver

    init {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    BaseActivity.BROADCAST_NETWORK_CHANGED -> {
                        ready = intent.getBooleanExtra(BaseActivity.ISCONNECTED, false)

                        if(ready){
                            preferred = true
                            db.preferred = false
                        }
                        else{
                            db.preferred = true
                        }
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(App.instance!!.applicationContext).
            registerReceiver(broadcastReceiver, IntentFilter(BaseActivity.BROADCAST_NETWORK_CHANGED))
    }

    override fun registerNewUser(
        user: DomainUser,
        password: String,
        onSuccess: () -> Unit
    ){
        try {
            FirebaseRegisterUserRequest()
                .execute(user, password, onSuccess)
        }
        catch (e : Exception){
            throw(e)
        }
     }

    override fun getUsers(callback: (ArrayList<DomainUser>) -> Unit){

        try{
            FirebaseGetUsersRequest(dataMapper, db).execute(callback)
        }
        catch (e: Exception){
            throw (e)
        }

    }

    override fun getCurrentUserId(callback: (uid: String?) -> Unit){

        GetCurrentUserIdRequest().execute(callback)
    }

    override fun fetchCurrentUser(callback: (user: DomainUser) -> Unit) {

        return FetchCurrentUserRequest().execute(callback)
    }

    override fun createChatRoom(chatRoom: DomainChatRoom, callback: (chatRoomId: String) -> Unit) {

        CreateChatRoomRequest().execute(chatRoom, callback)
    }

    override fun sendMessage(chatRoomId: String, message: DomainMessage, callback: () -> Unit){

        SendMessageRequest().execute(chatRoomId, message, callback)

    }

    override fun getLatestMessages(chatRooms: ArrayList<String>, callback: (ArrayList<DomainMessage>) -> Unit) {

        GetLatestMessagesRequest().execute(chatRooms, callback)
    }

    override fun getUserById(userId: String, callback: (DomainUser) -> Unit){

        GetUserByIdRequest().execute(userId, callback)
    }

    override fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<DomainUser>) -> Unit){

        GetMultipleUsersByIdRequest().execute(usersIds, callback)

    }

    override fun setMessageListener(chatRoomId: String, callback: (ArrayList<DomainMessage>) -> Unit){

        SetMessageListenerRequest().execute(chatRoomId, callback)
    }

    override fun addChatRoomIdToUsers(users: ArrayList<DomainUser>, chatRoomId: String, onSuccess: () -> Unit) {

        AddChatRoomIdToUsersRequest().execute(users, chatRoomId, onSuccess)
    }

    override fun updateUser(user: DomainUser, pictureChanged: Boolean, callback: ()->Unit){

        UpdateUserRequest().execute(user, pictureChanged, callback)
    }

    override fun deleteUser(userId: String, callback: () -> Unit){

        DeleteUserRequest().execute(userId, callback)
    }

    override fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (signInSuccessful: Boolean) -> Unit
    ) {

        SignInUserWithEmailAndPasswordRequest().execute(email, password, db, callback)
    }

    override fun logoutUser(callback: () -> Unit){

        LogoutUserRequest().execute(callback)
    }

    override fun verifyUserProfile(userId: String, callback: () -> Unit){

        VerifyUserProfileRequest().execute(userId, callback)
    }

    override fun makeUserAModerator(userId: String, callback: () -> Unit){

        MakeUserAModeratorRequest().execute(userId, callback)
    }

    override fun checkIfUserIsVerified(email: String, callback: (emailExists: Boolean, emailVerified: Boolean) -> Unit){

        CheckIfUserIsVerifiedRequest().execute(email, callback)
    }

    override fun updateLatestMessages(message: DomainMessage, callback: () -> Unit) {

        UpdateLatestMessageRequest().execute(message.chatRoomId, message, callback)

    }

}
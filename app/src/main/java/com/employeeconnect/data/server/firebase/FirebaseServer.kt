package com.employeeconnect.data.server.firebase


import com.employeeconnect.data.db.EmployeeConnectDb
import com.employeeconnect.domain.Models.User as DomainUser
import com.employeeconnect.domain.Models.Message as DomainMessage
import com.employeeconnect.domain.Models.ChatRoom as DomainChatRoom

import com.employeeconnect.domain.datasource.DataSource
import kotlin.collections.ArrayList

class FirebaseServer( private val dataMapper: FirebaseDataMapper = FirebaseDataMapper(),
                      private val db: EmployeeConnectDb = EmployeeConnectDb()
) : DataSource {

    companion object{
         val TAG = "FirebaseServerTag"
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

    override fun addChatRoomIdToUsers(users: ArrayList<DomainUser>, chatRoomId: String, callback: () -> Unit) {

        AddChatRoomIdToUsersRequest().execute(users, chatRoomId, callback)
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
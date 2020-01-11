package com.employeeconnect.data.Server.firebase

import com.employeeconnect.domain.Models.ChatRoom
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.datasource.DataSource
import kotlin.collections.ArrayList

class FirebaseServer : DataSource {

    companion object{
         val TAG = "FirebaseServerTag"
    }

    override fun registerNewUser(
        user: User,
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

    override fun getUsers(callback: (ArrayList<User>) -> Unit){

        try{
            FirebaseGetUsersRequest().execute(callback)
        }
        catch (e: Exception){
            throw (e)
        }

    }

    override fun getCurrentUserId(callback: (uid: String?) -> Unit){

        GetCurrentUserIdRequest().execute(callback)
    }

    override fun fetchCurrentUser() {

        FetchCurrentUserRequest().execute()
    }

    override fun createChatRoom(chatRoom: ChatRoom, callback: (chatRoomId: String) -> Unit) {

        CreateChatRoomRequest().execute(chatRoom, callback)
    }

    override fun sendMessage(chatRoomId: String, message: Message){

        SendMessageRequest().execute(chatRoomId, message)
    }

    override fun getLatestMessages(chatRooms: ArrayList<String>, callback: (ArrayList<Message>) -> Unit) {

        GetLatestMessagesRequest().execute(chatRooms, callback)
    }

    override fun getUserById(userId: String, callback: (User) -> Unit){

        GetUserByIdRequest().execute(userId, callback)
    }

    override fun getMultipleUsersById(usersIds: ArrayList<String>, callback: (ArrayList<User>) -> Unit){

        GetMultipleUsersByIdRequest().execute(usersIds, callback)
    }

    override fun setMessageListener(chatRoomId: String, callback: (ArrayList<Message>) -> Unit){

        SetMessageListenerRequest().execute(chatRoomId, callback)
    }

    override fun addChatRoomIdToUsers(users: ArrayList<User>, chatRoomId: String) {

        AddChatRoomIdToUsersRequest().execute(users, chatRoomId)
    }

    override fun updateUser(user: User, pictureChanged: Boolean, callback: ()->Unit){

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

        SignInUserWithEmailAndPasswordRequest().execute(email, password, callback)
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

}
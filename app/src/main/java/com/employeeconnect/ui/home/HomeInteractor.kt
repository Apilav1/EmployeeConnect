package com.employeeconnect.ui.home

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.*

class HomeInteractor {

    interface OnLoginFinishedListener {

        fun onSuccess()
        fun onError()
        fun onNoUserLoggedIn()
        fun onFetchingUserSuccess(user: User)
        fun onFetchingUsersSuccess(users: ArrayList<User>)
        fun onFetchingLatestMessagesSuccess(result: ArrayList<Message>)
        fun onFetchingMultipleUsersByIds(result: ArrayList<User>)
        fun onVerificationSuccess()
        fun onMakingUserAModeratorSuccess()
        fun onUpdateLatestMessageSuccess()
        fun onUpdateUserSuccessfully()
        fun onSuccessfulUserLogout()
        fun onSuccessfullUserDeletion()

    }

    fun verifyUserIsLoggedIn(listener: OnLoginFinishedListener) {

        GetCurrentUserIdCommand{result->

            if(result == null){
                listener.onNoUserLoggedIn()
            }
            else{
                FetchCurrentUserCommand{user ->
                    listener.onFetchingUserSuccess(user)
                }.execute()
            }

        }.execute()
    }

    fun getUsers(listener: OnLoginFinishedListener){

        GetUsersCommand {
            listener.onFetchingUsersSuccess(it)
        }.execute()

    }

    fun getLatestMessages(chatRoomsIds: ArrayList<String>, listener: OnLoginFinishedListener){

        var result: ArrayList<Message>

        GetLatestMessagesCommand(chatRoomsIds){ messages ->

                if(messages.size == 0) return@GetLatestMessagesCommand

                result = messages

                result.sortBy { it.timeStamp }

                listener.onFetchingLatestMessagesSuccess(result)

            }.execute()
        }

    fun getMultipleUsersByIds(usersIds: ArrayList<String>, listener: OnLoginFinishedListener){


       GetMultipleUsersByIdCommand(usersIds){ users ->

           listener.onFetchingMultipleUsersByIds(users)

       }.execute()
   }

    fun vefiryUser(userId: String, listener: OnLoginFinishedListener){

        VerifyUserCommand(userId){

            listener.onVerificationSuccess()

        }.execute()

    }

    fun makeUserAModerator(userId: String, listener: OnLoginFinishedListener){

        MakeUserAModeratorCommand(userId){

            listener.onMakingUserAModeratorSuccess()

        }.execute()

    }

    fun updateLatestMessage(message: Message, listener: OnLoginFinishedListener){

        UpdateLatestMessageCommand(message){

            listener.onUpdateLatestMessageSuccess()

        }.execute()

    }

    fun updateUser(user: User, pictureIsChanged: Boolean, listener: OnLoginFinishedListener){

        UpdateUserCommand(user, pictureIsChanged){

            listener.onUpdateUserSuccessfully()

        }.execute()

    }

    fun logoutUser(listener: OnLoginFinishedListener){

        LogoutUserCommand {

            listener.onSuccessfulUserLogout()

        }.execute()

    }

    fun deleteUser(user: User, listener: OnLoginFinishedListener){

        DeleteUserCommand(user.uid){

            listener.onSuccessfullUserDeletion()

        }.execute()
    }
}
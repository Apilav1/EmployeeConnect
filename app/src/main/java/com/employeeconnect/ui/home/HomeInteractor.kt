package com.employeeconnect.ui.home

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.*

class HomeInteractor {

    interface OnHomeListener {

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

    fun verifyUserIsLoggedIn(listener: OnHomeListener) {

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

    fun getUsers(listener: OnHomeListener){

        GetUsersCommand {
            listener.onFetchingUsersSuccess(it)
        }.execute()

    }

    fun getLatestMessages(chatRoomsIds: ArrayList<String>, listener: OnHomeListener){

        var result: ArrayList<Message>

        GetLatestMessagesCommand(chatRoomsIds){ messages ->

                if(messages.size == 0) return@GetLatestMessagesCommand

                result = messages

                result.sortBy { it.timeStamp }

                listener.onFetchingLatestMessagesSuccess(result)

            }.execute()
        }

    fun getMultipleUsersByIds(usersIds: ArrayList<String>, listener: OnHomeListener){


       GetMultipleUsersByIdCommand(usersIds){ users ->

           listener.onFetchingMultipleUsersByIds(users)

       }.execute()
   }

    fun vefiryUser(userId: String, listener: OnHomeListener){

        VerifyUserCommand(userId){

            listener.onVerificationSuccess()

        }.execute()

    }

    fun makeUserAModerator(userId: String, listener: OnHomeListener){

        MakeUserAModeratorCommand(userId){

            listener.onMakingUserAModeratorSuccess()

        }.execute()

    }

    fun updateLatestMessage(message: Message, listener: OnHomeListener){

        UpdateLatestMessageCommand(message){

            listener.onUpdateLatestMessageSuccess()

        }.execute()

    }

    fun updateUser(user: User, pictureIsChanged: Boolean, listener: OnHomeListener){

        UpdateUserCommand(user, pictureIsChanged){

            listener.onUpdateUserSuccessfully()

        }.execute()

    }

    fun logoutUser(listener: OnHomeListener){

        LogoutUserCommand {

            listener.onSuccessfulUserLogout()

        }.execute()

    }

    fun deleteUser(user: User, listener: OnHomeListener){

        DeleteUserCommand(user.uid){

            listener.onSuccessfullUserDeletion()

        }.execute()
    }
}
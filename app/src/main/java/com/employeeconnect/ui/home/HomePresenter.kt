package com.employeeconnect.ui.home

import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User

class HomePresenter (var homeView: HomeView?, var homeInteractor: HomeInteractor) : HomeInteractor.OnLoginFinishedListener{

    fun verifyUserIsLoggedIn(){
        homeInteractor.verifyUserIsLoggedIn(this)
    }

    override fun onSuccess() {

    }

    override fun onError() {
    }

    override fun onNoUserLoggedIn() {
        homeView?.noUserIsLoggedInError()
    }

    override fun onFetchingUserSuccess(user: User) {
        homeView!!.onFetchCurrentUser(user)
    }

    fun getUsers(){
        homeInteractor.getUsers(this)
    }

    override fun onFetchingUsersSuccess(users: ArrayList<User>) {
        homeView?.showUsers(users)
    }

    fun getLatestMessages(chatRoomsIds: ArrayList<String>){
        homeInteractor.getLatestMessages(chatRoomsIds, this)
    }

    override fun onFetchingLatestMessagesSuccess(result: ArrayList<Message>) {
        homeView?.onFetchingLatestMessages(result)
    }

    fun getMultipleUsersById(usersIds: ArrayList<String>){
        homeInteractor.getMultipleUsersByIds(usersIds, this)
    }

    override fun onFetchingMultipleUsersByIds(result: ArrayList<User>) {
        homeView?.onFetchingMultipleUsersByIds(result)
    }

    fun verifyUser(userId: String){
        homeInteractor.vefiryUser(userId, this)
    }

    override fun onVerificationSuccess() {
        homeView?.onVerificationSuccess()
    }

    fun makeUserAModerator(userId: String){
        homeInteractor.makeUserAModerator(userId, this)
    }

    override fun onMakingUserAModeratorSuccess() {
        homeView?.onMakingUserAModeratorSuccess()
    }

    fun updateLatestMessage(message: Message){
        homeInteractor.updateLatestMessage(message, this)
    }

    override fun onUpdateLatestMessageSuccess() {
        homeView?.showLatestMessageUpdated()
    }

    fun updateUser(user: User, pictureIsChanged: Boolean){
        homeInteractor.updateUser(user, pictureIsChanged, this)
    }

    override fun onUpdateUserSuccessfully() {
        homeView?.onUpdateUserSuccefully()
    }

    fun logoutUser(){
        homeInteractor.logoutUser(this)
    }

    override fun onSuccessfulUserLogout() {
        homeView?.onLogoutUser()
    }

    fun deleteUser(user: User){
        homeInteractor.deleteUser(user, this)
    }

    override fun onSuccessfullUserDeletion() {
        homeView?.onDeletionUser()
    }
}
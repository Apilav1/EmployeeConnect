package com.employeeconnect.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.employeeconnect.R
import com.employeeconnect.domain.Models.Message
import com.employeeconnect.domain.Models.User
import com.employeeconnect.networks.ConnectivityReceiver
import com.employeeconnect.ui.activities.BaseActivity
import com.employeeconnect.ui.chatLog.ChatLogActivity
import com.employeeconnect.ui.login.LoginActivity
import com.google.android.material.badge.BadgeDrawable
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), HomeView,
                                        EmployeesFragment.OnListEmployeesFragmentInteractionListener,
                                        LatestMessagesFragment.OnMessagesListFragmentInteractionListener,
                                        UserProfileFragment.OnUserProfileFragmentInteractionListener,
                                        ConnectivityReceiver.ConnectivityReceiverListener {


    private val presenter = HomePresenter(this, HomeInteractor())
    private var latestMessagesWithoutUsers: ArrayList<Message>? = null
    private var badge: BadgeDrawable? = null
    private var CHAT_LOG_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        currentFragmet = EmployeesFragment()

        presenter.verifyUserIsLoggedIn()

        bottom_navigation.setOnNavigationItemSelectedListener {

            return@setOnNavigationItemSelectedListener when (it.itemId){
                R.id.nav_home ->{
                    replaceFragment(EmployeesFragment())
                }
                R.id.nav_messages ->{
                    replaceFragment(LatestMessagesFragment())
                }
                R.id.nav_userprofile -> {
                    replaceFragment(UserProfileFragment())
                }
                else -> false
            }
        }

        badge = bottom_navigation.getOrCreateBadge(R.id.nav_messages)
        badge?.isVisible = false
    }

    private fun replaceFragment(fragment: Fragment): Boolean{

        if(!deviceIsConnected){
            Toast.makeText(applicationContext, "Please check your internet connection!",
                            Toast.LENGTH_SHORT).show()
            return false
        }

        currentFragmet = fragment

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_containter, fragment)
        fragmentTransaction.commitAllowingStateLoss()

        return true
    }

    private fun clearFragmentStack(){
        val fragmentManager = supportFragmentManager

        for(i in 0 until fragmentManager.backStackEntryCount)
            fragmentManager.popBackStack()
    }

    override fun onListEmployeesFragmentInteraction(user: User) {

    }

    override fun onListMessagesFragmentInteraction(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        //intent.putExtra(USER_KEY, user)   TransactionTooLargeException: data parcel size 805756 bytes
        toUser = user
        startActivityForResult(intent, CHAT_LOG_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CHAT_LOG_CODE){
            currentFragmet = LatestMessagesFragment()
            replaceFragment(currentFragmet!!)

            (currentFragmet as LatestMessagesFragment).showLatestMessages(messages)
        }
    }

    override fun onFetchCurrentUser(user: User) {
         currentUser = user
         presenter.getUsers()
    }

    override fun showUsers(users: ArrayList<User>) {

        if(users.size < 2){
            return
        }

        currentUsers = ArrayList()
        currentUsers = users
        replaceFragment(currentFragmet!!)

        val chatRoomsIds = ArrayList<String>()
        chatRoomsIds.addAll(currentUser?.chatRooms?.keys!!)

        presenter.getLatestMessages(chatRoomsIds)
    }

    override fun noUserIsLoggedInError() {
        val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
    }

    override fun showError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    override fun onFetchingLatestMessages(result: ArrayList<Message>) {

        latestMessagesWithoutUsers = ArrayList()
        latestMessagesWithoutUsers = result

        val toUsersIds = ArrayList<String>()

        latestMessagesWithoutUsers?.forEach {
            for((key, value) in currentUser!!.chatRooms){
                if(it.chatRoomId == key)
                    toUsersIds.add(value)
            }
        }
        presenter.getMultipleUsersById(toUsersIds)
    }

    override fun onFetchingMultipleUsersByIds(result: ArrayList<User>) {

        messages = HashMap()

        for(i in 0 until result.size){
            messages!![result[i]] = latestMessagesWithoutUsers?.get(i)!!
        }

        if(currentFragmet is LatestMessagesFragment){
            (currentFragmet as LatestMessagesFragment).showLatestMessages(messages)
        }

        handleBadges()
    }

    fun handleBadges(){
        var newMessages = 0

        latestMessagesWithoutUsers!!.forEach {
            if(it.toUser == currentUser!!.uid && !it.seen)
                newMessages++
        }


        badge?.number = newMessages

        badge?.isVisible = newMessages > 0
    }

    override fun updateMessageSeenField(message: Message) {
         presenter.updateLatestMessage(message)
    }

    override fun showLatestMessageUpdated() {

        val chatRoomsIds = ArrayList<String>()
        chatRoomsIds.addAll(currentUser?.chatRooms?.keys!!)

        presenter.getLatestMessages(chatRoomsIds)
    }

    override fun verifyUser(userId: String) {
        presenter.verifyUser(userId)
    }

    override fun onVerificationSuccess() {
        (currentFragmet as EmployeesFragment).onVerificationSuccess()
    }

    override fun makeUserAModerator(userId: String) {
        presenter.makeUserAModerator(userId)
    }

    override fun onMakingUserAModeratorSuccess() {
        (currentFragmet as EmployeesFragment).onMakingUserAModeratorSuccess()
    }

    override fun requestUsers() {
        if(currentUsers != null && (currentUsers?.size != 0 || currentUsers?.size != 1))
            (currentFragmet as EmployeesFragment).showUsers(currentUsers!!)
    }

    override fun requestLatestMessages() {
        if(messages != null && (messages?.size != 0 || messages?.size != 1) )
            (currentFragmet as LatestMessagesFragment).showLatestMessages(messages)
    }

    override fun updateUser(user: User, pictureIsChanged: Boolean) {
        presenter.updateUser(user, pictureIsChanged)
    }

    override fun onUpdateUserSuccefully() {
        Toast.makeText(applicationContext, "User updated succefully", Toast.LENGTH_SHORT).show()
    }

    override fun logoutUser(user: User) {
        presenter.logoutUser()
    }

    override fun onLogoutUser() {

        //clearFragmentStack()

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun deleteUser(user: User) {
        presenter.deleteUser(user)
    }

    override fun onDeletionUser() {

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        Toast.makeText(applicationContext, "This user profile is successfully deleted", Toast.LENGTH_SHORT).show()

    }

    companion object{
        private const val TAG =  "HomeActivity"

        var currentUser: User? = null

        var currentFragmet: Fragment? = null

        var currentUsers: ArrayList<User>? = null

        var messages: HashMap<User, Message>? = null

        var toUser: User? = null

        const val USER_KEY = "USER_KEY"
        const val USERS_KEY = "USERS_KEY"
    }
}

package com.employeeconnect.ui.Activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.employeeconnect.ui.Fragments.EmployeesFragment
import com.employeeconnect.ui.Fragments.LatestMessagesFragment
import com.employeeconnect.ui.Fragments.UserProfileFragment
import com.employeeconnect.R
import com.employeeconnect.data.Server.Config
import com.employeeconnect.data.Server.FirebaseServer
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.FetchCurrentUserCommand
import com.employeeconnect.domain.commands.GetCurrentUserIdCommand
import com.employeeconnect.networks.ConnectivityReceiver
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(),
                                        EmployeesFragment.OnListEmployeesFragmentInteractionListener,
                                        LatestMessagesFragment.OnMessagesListFragmentInteractionListener,
                                        UserProfileFragment.OnUserProfileFragmentInteractionListener,
                                        ConnectivityReceiver.ConnectivityReceiverListener {
//    lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        verifyUserIsLoggedIn()


//        mRegistrationBroadcastReceiver = object:BroadcastReceiver(){
//            override fun onReceive(context: Context?, intent: Intent?) {
//                Log.d(TAG, "primio")
//                if(intent!!.action == Config.STR_PUSH){
//                    Log.d(TAG, "IF ispunjen")
//                     val message = intent.getStringExtra("message")
//                     showNotification("New message", message.toString())
//                }
//            }
//
//        }


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
    }

    private fun verifyUserIsLoggedIn() {

//        viewModel.validate()dfsfsdfs

        GetCurrentUserIdCommand{result->

            currentUserId = result

            if(currentUserId == null){
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else{
                FetchCurrentUserCommand().execute()

                replaceFragment(currentFragmet)
            }

        }.execute()
    }


    private fun showNotification(tittle: String, message: String?) {
        Log.d(TAG, tittle+" "+message)

        val intent = Intent(applicationContext, HomeActivity::class.java)
        val contentIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //TODO: builder deprecated -> https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o
        val builder = NotificationCompat.Builder(applicationContext, "1")
        builder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(title)
            .setContentText(message)
            .setContentIntent(contentIntent)

        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, builder.build())
    }

    private fun replaceFragment(fragment: Fragment): Boolean{

        if(!deviceIsConnected){
            Toast.makeText(applicationContext, "Please check your internet connection!",
                            Toast.LENGTH_SHORT).show()
            return false
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_containter, fragment)
        fragmentTransaction.commit()

        return true
    }

    override fun onListEmployeesFragmentInteraction(user: User) {

    }

    override fun onListMessagesFragmentInteraction(user: User) {
        val intent = Intent(this, ChatLogActivity::class.java)
        currentFragmet = EmployeesFragment()
        startActivity(intent)
    }

    override fun onUserProfileFragmentInteraction(uri: Uri) {
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume()")
        currentFragmet = EmployeesFragment()
//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(mRegistrationBroadcastReceiver, IntentFilter("newMessage"))
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter(Config.STR_PUSH))

    }


    companion object{
        private const val TAG =  "HomeAkttt"

        var currentUser: User? = null
        var currentUserId: String? = null

        var currentFragmet: Fragment = EmployeesFragment()

        const val USER_KEY = "USER_KEY"
    }

}

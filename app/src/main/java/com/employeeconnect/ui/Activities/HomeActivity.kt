package com.employeeconnect.ui.Activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.employeeconnect.ui.Fragments.EmployeesFragment
import com.employeeconnect.ui.Fragments.MessagesFragment
import com.employeeconnect.ui.Fragments.UserProfileFragment
import com.employeeconnect.R
import com.employeeconnect.data.Server.FirebaseGetUsersRequest
import com.employeeconnect.data.dummy.DummyContent
import com.employeeconnect.domain.Models.User
import com.employeeconnect.domain.commands.GetUsersCommand
import com.employeeconnect.domain.commands.RegisterUserCommand
import com.employeeconnect.ui.view.UserRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.util.ArrayList


class HomeActivity : AppCompatActivity(),
                                        EmployeesFragment.OnListEmployeesFragmentInteractionListener,
                                        MessagesFragment.OnMessagesListFragmentInteractionListener,
                                        UserProfileFragment.OnUserProfileFragmentInteractionListener{

    private val TAG = "Home"
    private var bundle = Bundle.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        replaceFragment(EmployeesFragment())

        bottom_navigation.setOnNavigationItemSelectedListener {

            return@setOnNavigationItemSelectedListener when (it.itemId){
                R.id.nav_home ->{
                    replaceFragment(EmployeesFragment())
                    Log.d(TAG, "home pressed")
                    true
                }
                R.id.nav_messages ->{
                    replaceFragment(MessagesFragment())
                    Log.d(TAG, "messages pressed")
                    true
                }
                R.id.nav_userprofile -> {
                    Log.d(TAG, "userprofile pressed")
                    replaceFragment(UserProfileFragment())
                    true
                }
                else -> false
            }
        }
    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_containter, fragment)
        fragmentTransaction.commit()
    }

    override fun onListEmployeesFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListMessagesFragmentInteraction(item: com.employeeconnect.ui.Fragments.dummy.DummyContent.DummyItem?) {
        val intent = Intent(this, ChatLogActivity::class.java)
        startActivity(intent)
    }

    override fun onUserProfileFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.nav_menu, menu)
//
//        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView: SearchView? = searchItem?.actionView as SearchView
//
//        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        return super.onCreateOptionsMenu(menu)
//    }
   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(searchView?.getApplicationWindowToken(),
            InputMethodManager.SHOW_FORCED, 0
        )

        searchView?.requestFocus()

        searchView?.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        showKeyboard(searchView!!)
        searchView.setSubmitButtonEnabled(true)

        // Associate searchable configuration with the SearchView
        /*val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    //.forEach{ it.lowerCase().contains(newText.toLowerCase())

                }
                else{

                }
                return true
            }

        })*/
        return true
    }*/

    fun showKeyboard(ettext: SearchView) {
        Log.d("OVOO", "POKAZUJEM")
        ettext.requestFocus()
        ettext.postDelayed(Runnable {
            val keyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(ettext, 0)
        }
            , 200)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.nav_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        if(searchItem != null){
            Log.d("TAGGG", "it is not null")
            val searchView = searchItem.actionView as SearchView
            searchView.isClickable

            val searchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchableInfo = searchManager.getSearchableInfo(componentName)

            searchView.setSearchableInfo(searchableInfo)

            //val edittexthint = searchView.findViewById<EditText>(R.id.search_src_text)
            //edittexthint.hint = "seach here..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    if(newText!!.isNotEmpty()){
//                          val search = newText.toLowerCase()
//                            .forEach{
//                              if(it.toLowerCase().contrains(search))
//                            }
//                    }else{
//
//                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }
}

package com.example.kent.imessage


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.kent.imessage.fragments.contactsFragment
import com.example.kent.imessage.fragments.messagesFragment
import com.example.kent.imessage.fragments.profileFragment
import com.example.kent.imessage.fragments.settingsFragment
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class bottom_navigation : AppCompatActivity() {
    lateinit var choosenfragment:Fragment
   private val manager=supportFragmentManager
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {


        item ->

        when (item.itemId) {
            R.id.navigation_contacts -> {
               choosenfragment= contactsFragment()
                addFragment(choosenfragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_messages -> {
                choosenfragment= messagesFragment()
                addFragment(choosenfragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profle -> {
                choosenfragment= profileFragment()
                        addFragment(choosenfragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                choosenfragment= settingsFragment()
                addFragment(choosenfragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }


    private fun addFragment(fragment:Fragment) {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}


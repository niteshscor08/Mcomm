package com.mvine.mcomm.presentation.home.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setUpNavController()
        initListeneres()
    }
    private fun initListeneres() {
        activityHomeBinding.homeNavBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_chats -> navController.navigate(R.id.chatsFragment)
                R.id.action_calls -> navController.navigate(R.id.callsFragment)
                R.id.action_contacts -> navController.navigate(R.id.contactsFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        activityHomeBinding.homeNavBar.setupWithNavController(navController)
    }
}
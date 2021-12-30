package com.mvine.mcomm.presentation.home

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityHomeBinding
import com.mvine.mcomm.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setUpNavController()
        initListeners()
        checkPermissionsAndStartService()
    }
    private fun initListeners() {
        activityHomeBinding.homeNavBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_chats -> navController.navigate(R.id.chatsFragment)
                R.id.action_calls -> navController.navigate(R.id.callsFragment)
                R.id.action_contacts -> navController.navigate(R.id.contactsFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        activityHomeBinding.ivAppBarMenu.setOnClickListener {
            activityHomeBinding.tlSearch.visibility = View.GONE
            navController.navigate(R.id.loginMenuFragment)
        }

        activityHomeBinding.etSearch.apply {
            showKeyboard()
            doOnTextChanged { text, _, _, _ ->
                text?.let {
                    homeViewModel.searchLiveData.postValue(it.toString())
                }
            }
        }
    }
    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        activityHomeBinding.homeNavBar.setupWithNavController(navController)
    }

    fun showSearchBar() {
        activityHomeBinding.tlSearch.visibility = View.VISIBLE
    }

    private fun checkPermissionsAndStartService() {
        val permissions =
            arrayOf(
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.RECORD_AUDIO
            )
        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 101)
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}
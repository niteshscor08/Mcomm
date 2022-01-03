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
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.extension.decline
import com.mvine.mcomm.janus.extension.hangUp
import com.mvine.mcomm.janus.extension.pickup
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.INCOMING
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_ACCEPTED
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_DECLINING
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_HANGUP
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_INCOMING_CALL
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_REGISTERED
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.JANUS_REGISTRATION_FAILED
import com.mvine.mcomm.janus.utils.CommonStringValues.Companion.OUTGOING
import com.mvine.mcomm.presentation.audio.view.AudioDialogFragment
import com.mvine.mcomm.presentation.audio.view.AudioDialogListener
import com.mvine.mcomm.presentation.common.dialog.CallDialog
import com.mvine.mcomm.presentation.common.dialog.CallDialogData
import com.mvine.mcomm.presentation.common.dialog.CallDialogListener
import com.mvine.mcomm.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), CallDialogListener, AudioDialogListener {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController

    @Inject
    lateinit var janusManager: JanusManager

    @Inject
    lateinit var callState: CallState

    private lateinit var audioDialog : AudioDialogFragment

    var isRegistered : Boolean = false
    lateinit var callDialog: CallDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setUpNavController()
        initListeners()
        checkPermissionsAndStartService()
        startJanusSession()
        subscribeObservers()
        audioDialog = AudioDialogFragment(this, callState)
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

    private fun startJanusSession(){
        janusManager.connect()
    }

    private fun subscribeObservers(){
        janusManager.janusConnectionStatus.observe(this, {
            when(it){
                JANUS_REGISTERED -> {
                    isRegistered = true
                }
                JANUS_REGISTRATION_FAILED -> {
                    isRegistered = false
                }
                JANUS_INCOMING_CALL -> {
                    showCallsPopUp("", INCOMING, callState.remoteDisplayName, callState.remoteUrl)
                }
                JANUS_DECLINING, JANUS_HANGUP -> {
                    callDialog.dismiss()
                    audioDialog?.let { dialog ->
                        if(dialog.isVisible){
                            dialog.dismiss()
                        }
                    }
                }
                JANUS_ACCEPTED -> {
                    callDialog.dismiss()
                    audioDialog = AudioDialogFragment(this, callState)
                    audioDialog.show(this.supportFragmentManager, AudioDialogFragment::class.java.simpleName)
                }
            }
        })
    }

    fun showCallsPopUp(callerName: String, dialogType: String, displayName: String?, url : String?){
        if(dialogType == INCOMING){
            callDialog = CallDialog(
                callDialogListener= this,
                callDialogData = CallDialogData(callerName = callerName, isIncomingDialog = true),
                dialogType = INCOMING,
                callState = callState.apply {
                    remoteDisplayName = displayName
                    remoteUrl = url
                }
            )
            callDialog.show(this.supportFragmentManager, CallDialog::class.java.simpleName)
        }else{
            callDialog = CallDialog(
                callDialogListener= this,
                callDialogData = CallDialogData(callerName = callerName, isIncomingDialog = false),
                dialogType = OUTGOING,
                callState = callState.apply {
                    remoteDisplayName = displayName
                    remoteUrl = url
                }
            )
            callDialog.show(this.supportFragmentManager, CallDialog::class.java.simpleName)
        }
    }

    override fun onCallButtonClick() {
        janusManager.pickup()
        callDialog.dismiss()
    }

    override fun onCancelCallButtonClick(dialogType: String) {
        if(dialogType == INCOMING){
            janusManager.decline()
        }else{
            janusManager.hangUp()
        }
        callDialog.dismiss()
    }

    override fun onEndCallClick() {
        audioDialog.dismiss()
        janusManager.hangUp()
    }

}
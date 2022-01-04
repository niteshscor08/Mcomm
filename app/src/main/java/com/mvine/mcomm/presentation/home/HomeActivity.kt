package com.mvine.mcomm.presentation.home

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityHomeBinding
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.INCOMING
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.OUTGOING
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_ACCEPTED
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_DECLINING
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_HANGUP
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_INCOMING_CALL
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_REGISTERED
import com.mvine.mcomm.janus.commonvalues.JanusStatus.Companion.JANUS_REGISTRATION_FAILED
import com.mvine.mcomm.janus.extension.*
import com.mvine.mcomm.presentation.audio.view.AudioDialogFragment
import com.mvine.mcomm.presentation.audio.view.AudioDialogListener
import com.mvine.mcomm.presentation.common.base.BaseActivity
import com.mvine.mcomm.presentation.common.dialog.CallDialog
import com.mvine.mcomm.presentation.common.dialog.CallDialogData
import com.mvine.mcomm.presentation.common.dialog.CallDialogListener
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(), CallDialogListener, AudioDialogListener {

    @Inject
    lateinit var janusManager: JanusManager

    @Inject
    lateinit var callState: CallState

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var audioDialog : AudioDialogFragment

    lateinit var callDialog: CallDialog

    override val layoutId: Int
        get() =  R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    private fun setUp(){
        setUpNavController()
        initListeners()
        checkPermissionsAndStartService()
        initializeAudioScreen()
        subscribeObservers()
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding?.homeNavBar?.setupWithNavController(navController)
    }

    private fun initListeners() {
        binding?.homeNavBar?.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_chats -> navController.navigate(R.id.chatsFragment)
                R.id.action_calls -> navController.navigate(R.id.callsFragment)
                R.id.action_contacts -> navController.navigate(R.id.contactsFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        binding?.ivAppBarMenu?.setOnClickListener {
            binding?.tlSearch?.visibility = View.GONE
            navController.navigate(R.id.loginMenuFragment)
        }

        binding?.etSearch?.apply {
            showKeyboard()
            doOnTextChanged { text, _, _, _ ->
                text?.let {
                    homeViewModel.searchLiveData.postValue(it.toString())
                }
            }
        }
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

    private fun initializeAudioScreen(){
        audioDialog = AudioDialogFragment(this, callState)
    }

    private fun subscribeObservers(){
        janusManager.janusConnectionStatus.observe(this, {
            when(it){
                JANUS_REGISTRATION_FAILED -> {
                    dismissCallDialog()
                }
                JANUS_INCOMING_CALL -> {
                    showCallsPopUp(EMPTY_STRING, INCOMING, callState.remoteDisplayName, callState.remoteUrl)
                }
                JANUS_DECLINING, JANUS_HANGUP -> {
                    dismissCallDialog()
                    dismissAudioDialog()
                }
                JANUS_ACCEPTED -> {
                    dismissCallDialog()
                    audioDialog.show(this.supportFragmentManager, AudioDialogFragment::class.java.simpleName)
                }
                JANUS_REGISTERED -> {
                    if(callDialog.isVisible){
                        janusManager.call()
                    }
                    else{
                        janusManager.endJanusSession()
                    }
                }
            }
        })
    }

    fun showSearchBar() {
        binding?.tlSearch?.visibility = View.VISIBLE
    }

    fun startOutgoingCall(sTX: String, userName : String, uri : String) {
        showCallsPopUp(sTX, OUTGOING, userName, uri )
        janusManager.connect(sTX.toSIPRemoteAddress())
    }


    private fun showCallsPopUp(callerName: String, dialogType: String, displayName: String?, url : String?){
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
        }
        callDialog.show(this.supportFragmentManager, CallDialog::class.java.simpleName)
    }

    override fun onCallReceived() {
        janusManager.pickup()
        dismissCallDialog()
    }

    override fun onCallEnded(dialogType: String) {
        if(dialogType == INCOMING){
            janusManager.decline()
        }else{
            janusManager.hangUp()
        }
        dismissCallDialog()
    }

    override fun onCallHangUp() {
        janusManager.hangUp()
        dismissAudioDialog()
    }

    private fun dismissCallDialog(){
        callDialog?.let {
            if(it.isVisible){
                it.dismiss()
            }
        }
    }

    private fun dismissAudioDialog(){
        audioDialog?.let {
            if(it.isVisible){
                it.dismiss()
            }
        }
    }

}
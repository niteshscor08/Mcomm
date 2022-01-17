package com.mvine.mcomm.presentation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityHomeBinding
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.commonvalues.CallStatus
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.INCOMING
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.OUTGOING
import com.mvine.mcomm.janus.extension.*
import com.mvine.mcomm.presentation.audio.view.AudioActivity
import com.mvine.mcomm.presentation.common.base.BaseActivity
import com.mvine.mcomm.presentation.common.dialog.CallDialog
import com.mvine.mcomm.presentation.common.dialog.CallDialogData
import com.mvine.mcomm.presentation.common.dialog.CallDialogListener
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.hideKeyboard
import com.mvine.mcomm.util.showKeyboard
import com.mvine.mcomm.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(), CallDialogListener {

    @Inject
    lateinit var janusManager: JanusManager

    @Inject
    lateinit var callState: CallState

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController

    lateinit var callDialog: CallDialog

    private var isRegistered : Boolean = false

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
        startJanusSession()
        showLoginSuccessMessage()
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding?.homeNavBar?.setupWithNavController(navController)
    }

    private fun initListeners() {
        binding?.ivAppBarMenu?.setOnClickListener {
            binding?.etSearch?.hideKeyboard()
            val state = binding?.ivAppBarMenu?.isChecked
            state?.let {
                binding?.tlSearch?.isVisible = !it
                binding?.ivAppBarCreateGroup?.isVisible = !it
                if(it){
                    navController.navigate(R.id.loginMenuFragment)
                }else{
                    navController.popBackStack()
                }
            }
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
       // audioDialog = AudioDialogFragment(this, callState)
    }

    private fun subscribeObservers(){
        janusManager.janusConnectionStatus.observe(this, {
            when(it){
                CallStatus.REGISTRATION_FAILED.status -> {
                    dismissCallDialog()
                }
                CallStatus.INCOMING_CALL.status -> {
                    showCallsPopUp(EMPTY_STRING, INCOMING, callState.remoteDisplayName, callState.remoteUrl)
                }
                CallStatus.DECLINING.status, CallStatus.HANGUP.status -> {
                    homeViewModel.shouldRefreshData.postValue(true)
                    dismissCallDialog()
                }
                CallStatus.ACCEPTED.status -> {
                    dismissCallDialog()
                    startAudioActivity()
                }
                CallStatus.REGISTERED.status -> {
                    isRegistered = true
                }
            }
        })
    }

    fun showSearchBar() {
        binding?.homeNavBar?.visibility = View.VISIBLE
        binding?.etSearch?.hideKeyboard()
        binding?.etSearch?.text?.clear()
        binding?.tlSearch?.visibility = View.VISIBLE
    }

    fun startOutgoingCall(sTX: String, userName : String, uri : String) {
        if(isRegistered) {
            showCallsPopUp(sTX, OUTGOING, userName, uri)
            janusManager.call(sTX.toSIPRemoteAddress())
        }
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

    private fun dismissCallDialog(){
        callDialog?.let {
            if(it.isVisible){
                it.dismiss()
            }
        }
    }

    private fun startJanusSession(){
        janusManager.connect()
    }

    private fun endJanusSession(){
        janusManager.endJanusSession()
    }

    private fun showLoginSuccessMessage(){
       if(intent.extras?.isEmpty == false)
           binding?.let { showSnackBar(it.root, resources.getString(R.string.login_successful)) }
    }

    override fun onDestroy() {
        endJanusSession()
        super.onDestroy()
    }

    fun hideBottomTabBar(){
        binding?.homeNavBar?.visibility = View.GONE
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        homeViewModel.shouldRefreshData.postValue(true)
    }

    private fun startAudioActivity(){
        val intent = Intent(this, AudioActivity::class.java)
        resultLauncher.launch(intent)
    }

}
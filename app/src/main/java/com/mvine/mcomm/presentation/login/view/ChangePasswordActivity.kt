package com.mvine.mcomm.presentation.login.view

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityChangePasswordBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.login.viewmodel.ChangePasswordViewModel
import com.mvine.mcomm.util.hideKeyboard
import com.mvine.mcomm.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity: AppCompatActivity(), TextView.OnEditorActionListener {

    private lateinit var activityChangePasswordBinding: ActivityChangePasswordBinding

    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        setUp()
    }

    private fun setUp(){
        setSupportActionBar(activityChangePasswordBinding.toolBarChangePwd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activityChangePasswordBinding.changePasswordViewModel = changePasswordViewModel
        activityChangePasswordBinding.lifecycleOwner = this
        subscribeObservers()
        clickListener()
    }

    private fun subscribeObservers(){
        changePasswordViewModel.passwordUpdated.observe(this, {result ->
            activityChangePasswordBinding.etNewPassword.hideKeyboard()
            result?.let { response ->
                when(response){
                    is Resource.Success -> {
                        response.data?.let {
                            handlePasswordChangeResponse(it)

                        }
                    }
                    is Resource.Error -> {
                        activityChangePasswordBinding.progressBar.visibility = View.GONE
                        response.message?.let { showSnackBar(activityChangePasswordBinding.root, it, null, false) }
                    }
                    is Resource.Loading-> {
                        activityChangePasswordBinding.progressBar.visibility = View.VISIBLE
                    }
                }

            }

        })
    }

    private fun clickListener(){
        activityChangePasswordBinding.btnSubmit.setOnClickListener {
            callChangePasswordApi()
        }
        activityChangePasswordBinding.etReEnterPassword.setOnEditorActionListener(this)
    }

    private fun callChangePasswordApi(){
        activityChangePasswordBinding.root.hideKeyboard()
        if(validateInput()){
            changePasswordViewModel.callPasswordUpdateAPI()
        }
    }

    private fun validateInput(): Boolean{
        if(changePasswordViewModel.oldPassword.isNullOrEmpty() ||
            changePasswordViewModel.newPassword.isNullOrEmpty()||
            changePasswordViewModel.reEnteredPassword.isNullOrEmpty()){
            showSnackBar(activityChangePasswordBinding.root,
                resources.getString(R.string.empty_field_error), null, false)
            return false
        }
        if( changePasswordViewModel.oldPassword != changePasswordViewModel.getCredentialData().password){
            showSnackBar(
                activityChangePasswordBinding.root,
                resources.getString(R.string.old_pass_incorrect),
                resources.getString(R.string.old_pass_not_match),
                false
            )
            return false
        }
        if(changePasswordViewModel.newPassword != changePasswordViewModel.reEnteredPassword){
            showSnackBar(activityChangePasswordBinding.root,
                resources.getString(R.string.pass_mismatch), null, false)
            return false
        }

        return true
    }

    private fun handlePasswordChangeResponse(data: String) {
        activityChangePasswordBinding.progressBar.visibility = View.GONE
        if(data == resources.getString(R.string.ok)){
            changePasswordViewModel.updatePassword(
                activityChangePasswordBinding.etNewPassword.text.toString()
            )
            this?.setResult(Activity.RESULT_OK)
            this.finish()
        }else{
            showSnackBar(activityChangePasswordBinding.root, data, null, false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            callChangePasswordApi()
           return true
        }
        return false
    }

}
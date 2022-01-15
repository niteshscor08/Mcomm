package com.mvine.mcomm.presentation.login.view

import android.os.Bundle
import android.view.View
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
class ChangePasswordActivity: AppCompatActivity() {

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
            if(validateInput()){
                changePasswordViewModel.callPasswordUpdateAPI()
            }
        }
    }

    private fun validateInput(): Boolean{
        if(changePasswordViewModel.oldPassword.isNullOrEmpty() ||
            changePasswordViewModel.newPassword.isNullOrEmpty()||
            changePasswordViewModel.reEnteredPassword.isNullOrEmpty()){
            showSnackBar(activityChangePasswordBinding.root,
                resources.getString(R.string.empty_field_error), null, false)
            return false
        }else if( changePasswordViewModel.oldPassword != changePasswordViewModel.getCredentialData().password){
            showSnackBar(
                activityChangePasswordBinding.root,
                resources.getString(R.string.old_pass_incorrect),
                resources.getString(R.string.old_pass_not_match),
                false
            )
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
            showSnackBar(activityChangePasswordBinding.root, resources.getString(R.string.password_change) )
        }else{
            showSnackBar(activityChangePasswordBinding.root, data, null, false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
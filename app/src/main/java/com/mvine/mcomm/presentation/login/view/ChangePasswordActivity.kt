package com.mvine.mcomm.presentation.login.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityChangePasswordBinding
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.login.viewmodel.ChangePasswordViewModel
import com.mvine.mcomm.util.hideKeyboard
import com.mvine.mcomm.util.toast
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
    }

    private fun subscribeObservers(){
        changePasswordViewModel.passwordUpdated.observe(this, {result ->
            activityChangePasswordBinding.etNewPassword.hideKeyboard()
            result?.let { response ->
                when(response){
                    is Resource.Success -> {
                        changePasswordViewModel.updatePassword(
                            activityChangePasswordBinding.etNewPassword.text.toString()
                        )
                        activityChangePasswordBinding.progressBar.visibility = View.GONE
                        response.data?.let { this.toast(it) }
                    }
                    is Resource.Error -> {
                        activityChangePasswordBinding.progressBar.visibility = View.GONE
                        response.message?.let { this.toast(it) }
                    }
                    is Resource.Loading-> {
                        activityChangePasswordBinding.progressBar.visibility = View.VISIBLE
                    }
                }

            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
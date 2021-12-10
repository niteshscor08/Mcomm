package com.mvine.mcomm.presentation.login.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity: AppCompatActivity() {

    private lateinit var activityChangePasswordBinding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChangePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        setSupportActionBar(activityChangePasswordBinding.toolBarChangePwd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
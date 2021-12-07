package com.mvine.mcomm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mvine.mcomm.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Presenter Activity class that deals with the Login Activity
 */

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
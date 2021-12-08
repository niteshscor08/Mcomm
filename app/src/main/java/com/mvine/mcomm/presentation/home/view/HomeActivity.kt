package com.mvine.mcomm.presentation.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mvine.mcomm.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Presenter Activity class that deals with the Home Activity for Chats, Calls and Contacts
 */

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}
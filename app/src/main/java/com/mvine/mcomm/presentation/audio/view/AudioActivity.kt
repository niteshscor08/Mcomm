package com.mvine.mcomm.presentation.audio.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity() {

    private lateinit var activityAudioBinding: ActivityAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAudioBinding = DataBindingUtil.setContentView(this, R.layout.activity_audio)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
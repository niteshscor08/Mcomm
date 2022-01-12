package com.mvine.mcomm.presentation.audio.view

import android.os.Bundle
import android.os.CountDownTimer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ActivityAudioBinding
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.AudioFocusHandler
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.commonvalues.CallStatus
import com.mvine.mcomm.janus.extension.hangUp
import com.mvine.mcomm.presentation.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AudioActivity : BaseActivity<ActivityAudioBinding>() {

    @Inject
    lateinit var callState: CallState

    @Inject
    lateinit var janusManager: JanusManager

    @Inject
    lateinit var audioFocusHandler: AudioFocusHandler


    override val layoutId: Int
        get() = R.layout.activity_audio

    private var totalSecond = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    private fun setUp(){
        setToggleButtonState()
        configureTimer()
        setCallerData()
        setClickListener()
        subscribeObservers()
    }

    private fun setToggleButtonState(){
        binding?.let {
            it.bottomBarCallLayout.bbMic.isChecked = audioFocusHandler.getMicState()
            it.bottomBarCallLayout.bbSpeaker.isChecked = audioFocusHandler.getSpeakerState()
        }
    }

    private fun setCallerData(){
        callState.remoteDisplayName?.let {
            binding?.audioCallerName?.text = it
        }
        callState.remoteUrl?.let {
            val url = "${BuildConfig.BASE_URL}$it"
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
            binding?.let { it1 -> Glide.with(this).load(url).apply(options).into(it1.userImage) }
        }
    }

    private fun setClickListener(){
        binding?.let {
            it.bottomBarCallLayout.bbEndCall.setOnClickListener {
                janusManager.hangUp()
                finish()
            }
            it.bottomBarCallLayout.bbSpeaker.setOnCheckedChangeListener { _, isChecked ->
                audioFocusHandler.setSpeakerOn(isChecked)
            }

            it.bottomBarCallLayout.bbMic.setOnCheckedChangeListener { _, isChecked ->
                audioFocusHandler.setMicMuted(isChecked)
            }
        }
    }

    private fun subscribeObservers(){
        janusManager.janusConnectionStatus.observe(this, {
            when(it){
                CallStatus.DECLINING.status, CallStatus.HANGUP.status -> {
                    finish()
                }
            }
        })
    }

    private fun configureTimer(){
        val timer = object: CountDownTimer(2000000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdown()
            }
            override fun onFinish() {
            }
        }
        timer.start()
    }

    fun countdown() {
        var minutes: Int = 0
        var seconds: Int = 0
        totalSecond += 1
        minutes = (totalSecond % 3600) / 60
        seconds = (totalSecond % 3600) % 60
        var displayMin = String.format("%02d",minutes )
        var displaySec = String.format("%02d",seconds )
        binding?.timer?.text = "$displayMin:$displaySec"
    }

    override fun onDestroy() {
        audioFocusHandler.setMicMuted(false)
        audioFocusHandler.setSpeakerOn(false)
        binding?.let {
            it.bottomBarCallLayout.bbSpeaker.isChecked = false
            it.bottomBarCallLayout.bbMic.isChecked = false
        }
        super.onDestroy()
    }


}
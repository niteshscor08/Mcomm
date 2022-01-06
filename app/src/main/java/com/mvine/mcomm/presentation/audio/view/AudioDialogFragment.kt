package com.mvine.mcomm.presentation.audio.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentAudioDialogBinding
import com.mvine.mcomm.domain.model.CallState

class AudioDialogFragment(private val audioDialogListener: AudioDialogListener, private val callState: CallState) : DialogFragment(){

    private lateinit var audioDialogBinding: FragmentAudioDialogBinding
    private var totalSecond = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        audioDialogBinding = DataBindingUtil.inflate<FragmentAudioDialogBinding>(
            LayoutInflater.from(context),
            R.layout.fragment_audio_dialog,
            container,
            true
        )
        setUp()
        isCancelable = false
        return audioDialogBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callState.remoteDisplayName?.let {
            audioDialogBinding.audioCallerName.text = it
        }
        callState.remoteUrl?.let {
            val url = "${BuildConfig.BASE_URL}$it"
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
            Glide.with(this).load(url).apply(options).into(audioDialogBinding.userImage)
        }


    }

    private fun setUp(){
        audioDialogBinding.bottomBarCallLayout.bbEndCall.setOnClickListener {
            audioDialogListener.onCallHangUp()
        }
        configureTimer()
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
        audioDialogBinding.timer.text = "$displayMin:$displaySec"
    }

}
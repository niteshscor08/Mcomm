package com.mvine.mcomm.presentation.audio.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentAudioDialogBinding

class AudioDialogFragment(private val audioDialogListener: AudioDialogListener) : DialogFragment(){

    private lateinit var audioDialogBinding: FragmentAudioDialogBinding

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

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setUp(){
        audioDialogBinding.bottomBarCallLayout.bbEndCall.setOnClickListener {
            audioDialogListener.onEndCallClick()
        }
    }

}
package com.mvine.mcomm.janus

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager

class MediaPlayerHandler(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    init {
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        val fd = context.assets.openFd("dialing.ogg")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            fd.close()
            prepare()
            isLooping = true
        }
    }

    fun startCallDialing() {
        val fd = context.assets.openFd("dialing.ogg")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            fd.close()
            prepare()
            isLooping = true
            start()
        }
    }

    fun startRinging() {
        stopRinging()
        val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer.create(context, notificationUri).apply {
            isLooping = true
            start()
        }
    }

    fun stopRinging() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}

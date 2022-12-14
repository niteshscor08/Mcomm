package com.mvine.mcomm.janus

import android.content.Context
import android.opengl.EGLContext
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mvine.janusclient.IJanusGatewayCallbacks
import com.mvine.janusclient.JanusMediaConstraints
import com.mvine.janusclient.JanusPluginHandle
import com.mvine.janusclient.JanusServer
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.PreferenceHandler
import com.mvine.mcomm.util.USER_INFO
import org.json.JSONObject
import org.webrtc.PeerConnection
import java.lang.ref.WeakReference

class JanusManager(
    private val context: Context,
    private val preferenceHandler: PreferenceHandler,
    private val mediaPlayerHandler: MediaPlayerHandler,
    private val callState: CallState,
    private val audioFocusHandler: AudioFocusHandler
) {

    private val janusServer = JanusServer(JanusGlobalCallbacks())
    var handle: JanusPluginHandle? = null
    var personInfo: PersonInfo = preferenceHandler.get(USER_INFO)
    internal var jsep: JSONObject? = null
    private val _janusConnectionStatus: MutableLiveData<String> =
        MutableLiveData(EMPTY_STRING)
    val janusConnectionStatus: LiveData<String> = _janusConnectionStatus
    private val serviceHandler = ServiceHandler(WeakReference(this@JanusManager))

    val mediaConstraints = JanusMediaConstraints().apply {
        sendAudio = true
        recvAudio = true
        video = null
        recvVideo = true
    }

    fun connect() {
        createJanusSession()
        initializeMediaContext(
            context,
            audio = true,
            video = true,
            videoHwAcceleration = false
        )
    }

    private fun createJanusSession() {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            janusServer.Connect(cookie)
        }
    }

    private fun initializeMediaContext(
        context: Context?,
        audio: Boolean,
        video: Boolean,
        videoHwAcceleration: Boolean,
        eglContext: EGLContext? = null
    ): Boolean {
        return janusServer.initializeMediaContext(
            context,
            audio,
            video,
            videoHwAcceleration,
            eglContext
        )
    }

    inner class JanusGlobalCallbacks : IJanusGatewayCallbacks {
        override fun onCallbackError(error: String?) {
            Log.d(TAG, error.toString())
        }

        override fun onSuccess() {
            janusServer.Attach(
                JanusPluginCallbacks(
                    context,
                    this@JanusManager,
                    mediaPlayerHandler,
                    callState,
                    audioFocusHandler,
                    serviceHandler
                )
            )
        }

        override fun onDestroy() {
            Log.d(TAG, "onDestroy")
        }

        override fun getServerUri(): String {
            return BuildConfig.ENDPOINT
        }

        override fun getIceServers(): MutableList<PeerConnection.IceServer> {
            return ArrayList()
        }

        override fun getIpv6Support(): Boolean {
            return false
        }

        override fun getMaxPollEvents(): Int {
            return ZERO
        }
    }

    fun postConnectionStatus(status: String) {
        _janusConnectionStatus.postValue(status)
    }

    fun endJanusSession() {
        mediaPlayerHandler.stopRinging()
        audioFocusHandler.resetAudioControls()
        handle?.detach()
        janusServer?.Destroy()
    }

    fun startCallDialing() {
        audioFocusHandler.configureAudio(true)
        mediaPlayerHandler.startCallDialing()
    }

    companion object {
        val TAG: String = this.javaClass.name
        const val ZERO = 0
    }
}

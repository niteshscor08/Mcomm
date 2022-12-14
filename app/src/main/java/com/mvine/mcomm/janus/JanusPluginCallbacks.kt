package com.mvine.mcomm.janus

import android.content.Context
import android.util.Log
import com.mvine.janusclient.IJanusPluginCallbacks
import com.mvine.janusclient.JanusMessageType
import com.mvine.janusclient.JanusPluginHandle
import com.mvine.janusclient.JanusSupportedPluginPackages
import com.mvine.janusclient.PluginHandleWebRTCCallbacks
import com.mvine.mcomm.domain.model.CallState
import com.mvine.mcomm.janus.commonvalues.CallStatus
import com.mvine.mcomm.janus.extension.startSIPRegistration
import org.json.JSONObject
import org.webrtc.MediaStream

class JanusPluginCallbacks(
    private val context: Context,
    private val janusManager: JanusManager,
    private val mediaPlayerHandler: MediaPlayerHandler,
    private val callState: CallState,
    private val audioFocusHandler: AudioFocusHandler,
    private val serviceHandler: ServiceHandler
) : IJanusPluginCallbacks {

    override fun onCallbackError(error: String?) {
        Log.d(TAG, "$error")
    }

    override fun success(handle: JanusPluginHandle?) {
        janusManager.handle = handle
        handle?.h = serviceHandler
        janusManager.startSIPRegistration()
    }

    override fun onMessage(msg: JSONObject?, jsep: JSONObject?) {
        janusManager.jsep = jsep
        msg?.let {
            processReceivedMessage(it)
        }
        jsep?.let {
            processReceivedJSEP(it)
        }
    }

    override fun onLocalStream(stream: MediaStream?) {
        stream!!.audioTracks[0]
    }

    override fun onRemoteStream(stream: MediaStream?) {
        stream!!.apply {
            audioTracks[0].setEnabled(true)
            if (audioTracks[0].enabled())
                Log.d(TAG, "audio tracks enabled")
        }
    }

    override fun onDataOpen(data: Any?) {
        Log.d(TAG, "$data")
    }

    override fun onData(data: Any?) {
        Log.d(TAG, "$data")
    }

    override fun onCleanup() {
        Log.d(TAG, "onCleanup")
    }

    override fun onDetached() {
        Log.d(TAG, "onDetached")
    }

    override fun onMediaMessage(type: String?, receiving: Boolean) {
        if (type == "audio" && receiving) {
            Log.i(TAG, "onMediaMessage $type")
        }
    }

    override fun getPlugin(): JanusSupportedPluginPackages {
        return JanusSupportedPluginPackages.JANUS_SIP
    }

    private fun processReceivedMessage(msg: JSONObject) {
        when (msg.getString(SIP)) {
            JanusMessageType.event.toString() -> {
                if (msg.has(RESULT)) {
                    val result = msg.getJSONObject(RESULT)
                    val connectionStatus = result.getString(EVENT)
                    when (connectionStatus) {
                        CallStatus.ACCEPTED.status -> {
                            mediaPlayerHandler.stopRinging()
                            audioFocusHandler.configureAudio(true)
                        }
                        CallStatus.CALLING.status, CallStatus.RINGING.status -> {
                        }
                        CallStatus.DECLINING.status, CallStatus.HANGUP.status -> {
                            mediaPlayerHandler.stopRinging()
                        }
                        CallStatus.INCOMING_CALL.status -> {
                            configureCallState(result)
                            mediaPlayerHandler.startRinging()
                        }
                    }
                    janusManager.postConnectionStatus(connectionStatus)
                }
            }
        }
    }

    private fun processReceivedJSEP(jsep: JSONObject) {
        if (jsep != null) {
            janusManager.handle!!.handleRemoteJsep(object :
                    PluginHandleWebRTCCallbacks(janusManager.mediaConstraints, jsep, false) {
                    override fun onSuccess(obj: JSONObject?) {
                        Log.d(TAG, "Success %s $obj")
                    }
                    override fun onCallbackError(error: String?) {
                        Log.i(TAG, error.toString())
                    }
                })
        }
    }

    private fun configureCallState(result: JSONObject) {
        callState.remoteDisplayName = result.getString(DISPLAY_NAME).trim('"')
        callState.remoteUsername = result.getString(USERNAME)
        callState.remoteUrl = URL_PATH
    }

    companion object {
        val TAG: String = this.javaClass.name
        const val SIP = "sip"
        const val RESULT = "result"
        const val EVENT = "event"
        const val DISPLAY_NAME = "displayname"
        const val USERNAME = "username"
        const val URL_PATH = "/images/profile.gif"
    }
}

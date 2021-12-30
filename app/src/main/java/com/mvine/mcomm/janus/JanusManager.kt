package com.mvine.mcomm.janus

import android.content.Context
import android.content.Intent
import android.media.*
import android.opengl.EGLContext
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mvine.janusclient.*
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.janus.request.JanusRegister
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_ACCEPTED
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_CALLING
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_DECLINING
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_HANGUP
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_INCOMING_CALL
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_REGISTERED
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_REGISTRATION_FAILED
import com.mvine.mcomm.janus.utils.CommonValues.JANUS_RINGING
import com.mvine.mcomm.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import javax.inject.Inject

class JanusManager(private val context: Context, private val preferenceHandler: PreferenceHandler) {

    private val janusServer = JanusServer(JanusGlobalCallbacks())
    private var handle: JanusPluginHandle? = null
    private var previousAudioMode: Int? = null
    private lateinit var audioManager: AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null
    private var previousMicrophoneMute: Boolean? = null
    private var mediaPlayer: MediaPlayer? = null
    private var jsep: JSONObject?= null
    private val _janusConnectionStatus: MutableLiveData<String> =
        MutableLiveData(EMPTY_STRING)
    val janusConnectionStatus: LiveData<String> = _janusConnectionStatus


    private val mediaConstraints = JanusMediaConstraints().apply {
        sendAudio = true
        recvAudio = true
        video = null
        recvVideo = true
    }

    fun connect() {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        previousAudioMode = audioManager.mode
        createJanusSession()
        initializeMediaContext(context, true, true, false)
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer(){
        val fd = context.assets.openFd("dialing.ogg")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            fd.close()
            prepare()
            isLooping = true
        }
    }

    private fun createJanusSession() {
        val sharedPreferences = context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            janusServer.Connect(cookie)
        }
    }

    inner class JanusGlobalCallbacks : IJanusGatewayCallbacks {
        override fun onCallbackError(error: String?) {
            Log.d("JanusManager", error.toString())
        }

        override fun onSuccess() {
            janusServer.Attach(JanusPluginCallbacks())
        }

        override fun onDestroy() {
            TODO("Not yet implemented")
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
            return 0
        }

    }

    inner class JanusPluginCallbacks : IJanusPluginCallbacks {
        override fun onCallbackError(error: String?) {
            Log.d("Janus Manager", "onCallbackError, $error")
        }

        override fun success(handle: JanusPluginHandle?) {
            this@JanusManager.handle = handle
            startSIPRegistration()
        }

        override fun onMessage(msg: JSONObject?, jsep: JSONObject?) {
            when (msg?.getString("sip")) {
                JanusMessageType.event.toString() -> {
                    if (msg.has("result")) {
                        val result = msg.getJSONObject("result")
                        when (result.getString("event")) {
                            JANUS_REGISTRATION_FAILED -> {
                                _janusConnectionStatus.postValue(JANUS_REGISTERED)
                            }
                            JANUS_REGISTERED -> {
                                _janusConnectionStatus.postValue(JANUS_REGISTERED)
                            }
                            JANUS_ACCEPTED -> {
                                configureAudio(true)
                                _janusConnectionStatus.postValue(JANUS_ACCEPTED)
                                stopRinging()
                            }
                            JANUS_CALLING -> {
                                startRinging()
                                _janusConnectionStatus.postValue(JANUS_CALLING)
                            }
                            JANUS_RINGING -> {
                                startRinging()
                                _janusConnectionStatus.postValue(JANUS_RINGING)
                            }
                            JANUS_DECLINING -> {
                                _janusConnectionStatus.postValue(JANUS_DECLINING)
                                stopRinging()
                            }
                            JANUS_HANGUP -> {
                                _janusConnectionStatus.postValue(JANUS_HANGUP)
                                stopRinging()
                            }
                            JANUS_INCOMING_CALL -> {
                                this@JanusManager.jsep = jsep
                               // pickup(jsep)
                            }

                        }
                    }
                }
            }

            if (jsep != null) {
                handle!!.handleRemoteJsep(object :
                    PluginHandleWebRTCCallbacks(mediaConstraints, jsep, false) {

                    override fun onSuccess(obj: JSONObject?) {
                        Log.d("JanusManager", "Success %s $obj")
                    }

                    override fun onCallbackError(error: String?) {
                        Log.i("JanusManager", error.toString())
                    }
                })
            }
        }



    private fun configureAudio(enable: Boolean) {
        if (enable) {

            if (previousAudioMode == null)
                previousAudioMode = audioManager.mode
            // Request audio focus before making any device switch
            requestAudioFocus()
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            /*
             * Always disable microphone mute during a WebRTC call.
             */
            if (previousMicrophoneMute == null)
                previousMicrophoneMute = audioManager.isMicrophoneMute
        } else {
            audioManager.mode = previousAudioMode ?: AudioManager.MODE_NORMAL
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest?.let {
                    audioManager.abandonAudioFocusRequest(it)
                }
            } else {
                audioManager.abandonAudioFocus(null)
            }
            audioManager.isMicrophoneMute = previousMicrophoneMute ?: true
        }
    }

        private fun requestAudioFocus() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val playbackAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener({})
                    .build()
                audioManager.requestAudioFocus(request)
                audioFocusRequest = request
            } else {
                audioManager.requestAudioFocus(
                    null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                )
            }
        }


    override fun onLocalStream(stream: MediaStream?) {
            stream!!.audioTracks[0]
        }

        override fun onRemoteStream(stream: MediaStream?) {
            stream!!.apply {
                audioTracks[0].setEnabled(true)
                if (audioTracks[0].enabled())
                    Log.d("JanusManager","audio tracks enabled")
            }
        }

        override fun onDataOpen(data: Any?) {
            TODO("Not yet implemented")
        }

        override fun onData(data: Any?) {
            TODO("Not yet implemented")
        }

        override fun onCleanup() {
            TODO("Not yet implemented")
        }

        override fun onDetached() {
            TODO("Not yet implemented")
        }

        override fun onMediaMessage(type: String?, receiving: Boolean) {
            if (type == "audio" && receiving) {
                Log.i("JanusManager", "onMediaMessage $type")
            }
        }

        override fun getPlugin(): JanusSupportedPluginPackages {
            return JanusSupportedPluginPackages.JANUS_SIP
        }
    }

    private fun startSIPRegistration() {
        val personInfo: PersonInfo = preferenceHandler.get(USER_INFO)
        var obj: JSONObject?= null
            personInfo.view.apply {
                obj =  JSONObject().apply {
                put("request", "register")
                put("authuser", nSTX)
                put("username", "sip:$nSTX@portaluat.mvine.com")
                put("secret", nastrpass)
                put("display_name", contact?.name)
                put("proxy", "sip:portaluat.mvine.com:5068")
            }
        }
        val message = JSONObject().apply {
            put("message", obj)
        }
        handle?.sendMessage(object : PluginHandleSendMessageCallbacks(message) {
            override fun onSuccessSynchronous(obj: JSONObject?) {
                super.onSuccessSynchronous(obj)
            }

            override fun onCallbackError(error: String?) {
                super.onCallbackError(error)
                Log.d("JanusManager========", error.toString())
            }

            override fun onSuccesAsynchronous() {
                super.onSuccesAsynchronous()
                Log.d("JanusManager", "onSuccesAsynchronous")
            }
        })

    }

    private fun initializeMediaContext(
        context: Context?,
        audio: Boolean,
        video: Boolean,
        videoHwAcceleration: Boolean,
        eglContext: EGLContext?= null
    ): Boolean {
        return janusServer.initializeMediaContext(
            context,
            audio,
            video,
            videoHwAcceleration,
            eglContext
        )
    }

    fun call(callerInfo: String) {
        mediaPlayer?.start()
        try {
            val remoteAddress =  "sip:42010@portaluat.mvine.com:5068" // "sip:$callerInfo@${BuildConfig.SIP}"
            var jesp: JSONObject? = null
            Log.d("JanusMangaer", remoteAddress)
            this@JanusManager.handle!!.createOffer(object :
                PluginHandleWebRTCCallbacks(mediaConstraints, null, true) {
                override fun onSuccess(obj: JSONObject?) {

                    Log.d("JanusMangaer","createOffer.onSuccess $obj")
                    val msg = JSONObject()
                    val body = JSONObject()
                    body.put("request", "call")
                    var addr = remoteAddress
                    body.put("uri", addr)
                    msg.put("message", body)
                    msg.put("jsep", obj)
                    this@JanusManager.handle!!.sendMessage(object :
                        PluginHandleSendMessageCallbacks(msg) {
                        override fun onSuccessSynchronous(obj: JSONObject?) {
                            super.onSuccessSynchronous(obj)
                            Log.d("JanusMangaer","PluginHandleSendMessageCallbacks.onSuccessSynchronous $obj")
                        }

                        override fun onSuccesAsynchronous() {
                            super.onSuccesAsynchronous()
                            Log.d("JanusMangaer","PluginHandleSendMessageCallbacks.onSuccesAsynchronous")
                        }

                        override fun getMessage(): JSONObject {
                            Log.d("JanusMangaer","PluginHandleSendMessageCallbacks.getMessage")
                            return super.getMessage()
                        }

                        override fun onCallbackError(error: String?) {
                            super.onCallbackError(error)
                            Log.d("JanusMangaer","PluginHandleSendMessageCallbacks: $error")
                            handle!!.hangUp()
                        }
                    })
                }
                override fun onCallbackError(error: String?) {
                    Log.e("JanusMangaer","onCallBackError: $error")
                }
            })
        } catch (ex: Exception) {
            Log.i("JanusMangaer",ex.toString())
        }

    }

    fun startRinging() {
        stopRinging()
        val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer.create(context, notificationUri).apply {
            //            prepare()
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

    fun pickup() {
        handle!!.createAnswer(object : PluginHandleWebRTCCallbacks(mediaConstraints, jsep, true) {
            override fun onSuccess(obj: JSONObject?) {
                super.onSuccess(obj)
                Log.d("Janus Manager","createAnswer.onSuccess $obj")

                val body = JSONObject().apply { put("request", "accept") }
                val msg = JSONObject().apply {
                    put("message", body)
                    put("jsep", obj)
                }
                handle!!.sendMessage(object : PluginHandleSendMessageCallbacks(msg) {
                    override fun onSuccessSynchronous(obj: JSONObject?) {
                        super.onSuccessSynchronous(obj)
                        Log.d("Janus Manager","PluginHandleSendMessageCallbacks.onSuccessSynchronous $obj")
                    }

                    override fun onSuccesAsynchronous() {
                        super.onSuccesAsynchronous()
                        Log.d("Janus Manager","PluginHandleSendMessageCallbacks.onSuccessAsynchronous")
                    }

                    override fun getMessage(): JSONObject {
                        Log.d("Janus Manager","PluginHandleSendMessageCallbacks.getMessage")
                        return super.getMessage()
                    }

                    override fun onCallbackError(error: String?) {
                        super.onCallbackError(error)
                        Log.d("Janus Manager","PluginHandleSendMessageCallbacks: $error")
                    }
                })
            }

            override fun onCallbackError(error: String?) {
                super.onCallbackError(error)
                Log.d("Janus Manager","createAnswer.onCallbackError: $error")
            }
        })
    }
}
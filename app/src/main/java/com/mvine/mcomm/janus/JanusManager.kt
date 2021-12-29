package com.mvine.mcomm.janus

import android.content.Context
import android.util.Log
import com.mvine.janusclient.*
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.janus.request.JanusRegister
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.MCOMM_SHARED_PREFERENCES
import com.mvine.mcomm.util.PreferenceHandler
import com.mvine.mcomm.util.USER_INFO
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import javax.inject.Inject

class JanusManager @Inject constructor(@ApplicationContext private val context: Context) {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

     private val janusServer = JanusServer(JanusGlobalCallbacks())

    fun connect() {
        createJanusSession()
    }

    private fun createJanusSession() {
        val sharedPreferences = context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            janusServer.Connect(cookie)
        }
    }

    inner class JanusGlobalCallbacks : IJanusGatewayCallbacks {
        override fun onCallbackError(error: String?) {
            TODO("Not yet implemented")
        }

        override fun onSuccess() {
            janusServer.Attach(JanusPluginCallbacks())
        }

        override fun onDestroy() {
            TODO("Not yet implemented")
        }

        override fun getServerUri(): String {
            return ""
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
            TODO("Not yet implemented")
        }

        override fun success(handle: JanusPluginHandle?) {
            startSIPRegistration()
        }

        override fun onMessage(msg: JSONObject?, jsep: JSONObject?) {
            when (msg?.getString("sip")) {
                JanusMessageType.event.toString() -> {
                    if (msg.has("result")) {
                        val result = msg.getJSONObject("result")
                        when (result.getString("event")) {
                            "registered" -> Log.d("observeConnection", "Registered")
                        }
                    }
                }
            }
        }

        override fun onLocalStream(stream: MediaStream?) {
            TODO("Not yet implemented")
        }

        override fun onRemoteStream(stream: MediaStream?) {
            TODO("Not yet implemented")
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
            TODO("Not yet implemented")
        }

        override fun getPlugin(): JanusSupportedPluginPackages {
            return JanusSupportedPluginPackages.JANUS_SIP
        }
    }

    private fun startSIPRegistration(): JanusRegister {
        val personInfo: PersonInfo = preferenceHandler.get(USER_INFO)
        personInfo.view.apply {
            return JanusRegister(
                authuser = nSTX!!,
                display_name = contact?.name!!,
                secret = nastrpass!!,
                username = usename!!
            )
        }
    }
}
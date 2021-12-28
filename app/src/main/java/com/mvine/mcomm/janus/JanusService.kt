package com.mvine.mcomm.janus

import android.util.Log
import com.google.gson.Gson
import com.mvine.janusclient.*
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.janus.request.JanusCreate
import com.mvine.mcomm.janus.request.JanusRegister
import com.mvine.mcomm.janus.utils.CommonValues.CREATE_KEY_LENGTH
import com.mvine.mcomm.janus.utils.RandomString
import com.mvine.mcomm.util.PreferenceHandler
import com.mvine.mcomm.util.USER_INFO
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import javax.inject.Inject

class JanusService(private val webSocketService: WebSocketService) {

    @Inject
    lateinit var preferenceHandler: PreferenceHandler

    private val janusServer = JanusServer(JanusGlobalCallbacks())


    fun connect() {
        webSocketService.observeConnection()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Log.d("observeConnection", response.toString())
                onReceiveResponseConnection(response)
            }, { error ->
                Log.e("observeConnection", error.message.orEmpty())
            })
    }

    private fun onReceiveResponseConnection(response: WebSocket.Event) {
        when (response) {
            is WebSocket.Event.OnConnectionOpened<*> -> Log.i("connection", "opened")
            is WebSocket.Event.OnConnectionClosed -> Log.i("connection", "closed")
            is WebSocket.Event.OnConnectionClosing -> Log.i("connection", "closing connection")
            is WebSocket.Event.OnConnectionFailed -> Log.i("connection", "failed")
            is WebSocket.Event.OnMessageReceived -> handleOnMessageReceived(response.message)
        }
    }

    private fun handleOnMessageReceived(message: Message) {
        Log.i("connection", message.toString())
        when(message.toString()){
             JanusMessageType.success.toString() -> {
                when()
            }
        }
    }

    fun sendMessage(message: String) {
        webSocketService.sendMessage(message)
    }

    fun createJanusSession() {
        sendMessage(Gson().toJson(JanusCreate()))
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
            TODO("Not yet implemented")
        }

        override fun getIceServers(): MutableList<PeerConnection.IceServer> {
            TODO("Not yet implemented")
        }

        override fun getIpv6Support(): Boolean {
            TODO("Not yet implemented")
        }

        override fun getMaxPollEvents(): Int {
            TODO("Not yet implemented")
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
            TODO("Not yet implemented")
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

    fun startSIPRegistration() {
        val personInfo : PersonInfo = preferenceHandler.get(USER_INFO)
        
    }

}
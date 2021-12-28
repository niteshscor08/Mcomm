package com.mvine.mcomm.janus

import android.util.Log
import com.mvine.mcomm.janus.request.JanusCreate
import com.mvine.mcomm.janus.utils.CommonValues.CREATE_KEY_LENGTH
import com.mvine.mcomm.janus.utils.RandomString
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import io.reactivex.android.schedulers.AndroidSchedulers

class JanusService(private val webSocketService: WebSocketService) {

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
    }

    fun sendMessage(message: String) {
        webSocketService.sendMessage(JanusCreate(transaction = RandomString().randomString(CREATE_KEY_LENGTH)))
    }

}
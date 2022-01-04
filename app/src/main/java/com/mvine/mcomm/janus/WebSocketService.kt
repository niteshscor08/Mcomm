package com.mvine.mcomm.janus

import com.mvine.mcomm.janus.request.JanusCreate
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface WebSocketService {

    @Receive
    fun observeConnection(): Flowable<WebSocket.Event>

    @Send
    fun sendMessage(param: String)

    @Send
    fun sendMessage(param: JanusCreate)
}
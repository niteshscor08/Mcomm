package com.mvine.mcomm.service

import com.mvine.mcomm.presentation.home.chats.JanusMessage
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
    fun sendMessage(param: JanusMessage)
}
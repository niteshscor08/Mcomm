package com.mvine.mcomm.presentation.home.chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.FragmentChatsBinding
import com.mvine.mcomm.service.WebSocketService
import com.tinder.scarlet.*
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private val chatsViewModel: ChatsViewModel by viewModels()

    private lateinit var fragmentChatsBinding: FragmentChatsBinding

    @Inject
    lateinit var webSocketService: WebSocketService


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentChatsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        return fragmentChatsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setupWebSocketService()

        observeConnection()
        fragmentChatsBinding.send.setOnClickListener {
            sendMessage("hello")
        }
    }

    @SuppressLint("CheckResult")
    private fun observeConnection() {
        webSocketService.observeConnection()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Log.d("observeConnection", response.toString())
                onReceiveResponseConnection(response)
            }, { error ->
                Log.e("observeConnection", error.message.orEmpty())
                Snackbar.make(fragmentChatsBinding.root, error.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
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

    private fun sendMessage(message: String) {
        webSocketService.sendMessage(JanusMessage())
    }

}
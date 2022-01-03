package com.mvine.mcomm.janus.extension

import android.util.Log
import com.mvine.janusclient.PluginHandleSendMessageCallbacks
import com.mvine.janusclient.PluginHandleWebRTCCallbacks
import com.mvine.mcomm.janus.JanusManager
import org.json.JSONObject

fun JanusManager.call(sipRemoteAddress: String) {
    try {
        var jesp: JSONObject? = null
        Log.d("JanusManager", sipRemoteAddress)
        handle!!.createOffer(object :
            PluginHandleWebRTCCallbacks(mediaConstraints, null, true) {
            override fun onSuccess(obj: JSONObject?) {

                Log.d("JanusManager","createOffer.onSuccess $obj")
                val msg = JSONObject()
                val body = JSONObject()
                body.put("request", "call")
                var addr = sipRemoteAddress
                body.put("uri", addr)
                msg.put("message", body)
                msg.put("jsep", obj)
                handle!!.sendMessage(object :
                    PluginHandleSendMessageCallbacks(msg) {
                    override fun onSuccessSynchronous(obj: JSONObject?) {
                        super.onSuccessSynchronous(obj)
                        Log.d("JanusManager","PluginHandleSendMessageCallbacks.onSuccessSynchronous $obj")
                    }

                    override fun onSuccesAsynchronous() {
                        super.onSuccesAsynchronous()
                        Log.d("JanusManager","PluginHandleSendMessageCallbacks.onSuccesAsynchronous")
                    }

                    override fun getMessage(): JSONObject {
                        Log.d("JanusManager","PluginHandleSendMessageCallbacks.getMessage")
                        return super.getMessage()
                    }

                    override fun onCallbackError(error: String?) {
                        super.onCallbackError(error)
                        Log.d("JanusManager","PluginHandleSendMessageCallbacks: $error")
                        handle!!.hangUp()
                    }
                })
            }
            override fun onCallbackError(error: String?) {
                Log.e("JanusManager","onCallBackError: $error")
            }
        })
    } catch (ex: Exception) {
        Log.i("JanusManager",ex.toString())
    }

}

fun JanusManager.pickup() {
    val jsep: JSONObject? = jsep
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

fun JanusManager.hangUp() {
        val msg  = JSONObject()
        val body = JSONObject()
        body.put("request", "hangup")
        msg.put("message", body)
        handle!!.sendMessage(PluginHandleSendMessageCallbacks(msg))
}

fun JanusManager.decline() {
    val msg = JSONObject()
    val body = JSONObject()
    body.put("request", "decline")
    msg.put("message", body)
    handle!!.sendMessage(PluginHandleSendMessageCallbacks(msg))
}
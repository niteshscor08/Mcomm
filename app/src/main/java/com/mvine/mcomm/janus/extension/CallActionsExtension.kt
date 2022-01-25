package com.mvine.mcomm.janus.extension

import android.util.Log
import com.mvine.janusclient.PluginHandleSendMessageCallbacks
import com.mvine.janusclient.PluginHandleWebRTCCallbacks
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.JanusManager.Companion.TAG
import com.mvine.mcomm.janus.commonvalues.CallStatus
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.JSEP
import com.mvine.mcomm.janus.commonvalues.CommonValues.Companion.URI
import org.json.JSONObject

fun JanusManager.call(sipRemoteAddress: String) {
    try {
        startCallDialing()
        Log.d(TAG, sipRemoteAddress)
        handle!!.createOffer(object :
                PluginHandleWebRTCCallbacks(mediaConstraints, null, true) {
                override fun onSuccess(obj: JSONObject?) {
                    Log.d(TAG, "createOffer.onSuccess $obj")
                    val msg = JSONObject()
                    val body = JSONObject()
                    body.put(CallStatus.REQUEST.status, CallStatus.CALL.status)
                    var addr = sipRemoteAddress
                    body.put(URI, addr)
                    msg.put(CallStatus.MESSAGE.status, body)
                    msg.put(JSEP, obj)
                    handle!!.sendMessage(object :
                            PluginHandleSendMessageCallbacks(msg) {
                            override fun onSuccessSynchronous(obj: JSONObject?) {
                                super.onSuccessSynchronous(obj)
                                Log.d(TAG, "PluginHandleSendMessageCallbacks.onSuccessSynchronous $obj")
                            }

                            override fun onSuccesAsynchronous() {
                                super.onSuccesAsynchronous()
                                Log.d(TAG, "PluginHandleSendMessageCallbacks.onSuccesAsynchronous")
                            }

                            override fun getMessage(): JSONObject {
                                Log.d(TAG, "PluginHandleSendMessageCallbacks.getMessage")
                                return super.getMessage()
                            }

                            override fun onCallbackError(error: String?) {
                                super.onCallbackError(error)
                                Log.d(TAG, "PluginHandleSendMessageCallbacks: $error")
                                handle!!.hangUp()
                            }
                        })
                }
                override fun onCallbackError(error: String?) {
                    Log.e(TAG, "onCallBackError: $error")
                }
            })
    } catch (ex: Exception) {
        Log.i(TAG, ex.toString())
    }
}

fun JanusManager.pickup() {
    val jsep: JSONObject? = jsep
    handle!!.createAnswer(object : PluginHandleWebRTCCallbacks(mediaConstraints, jsep, true) {
        override fun onSuccess(obj: JSONObject?) {
            super.onSuccess(obj)
            Log.d(TAG, "createAnswer.onSuccess $obj")

            val body = JSONObject().apply { put(CallStatus.REQUEST.status, CallStatus.ACCEPT.status) }
            val msg = JSONObject().apply {
                put(CallStatus.MESSAGE.status, body)
                put(JSEP, obj)
            }
            handle!!.sendMessage(object : PluginHandleSendMessageCallbacks(msg) {
                override fun onSuccessSynchronous(obj: JSONObject?) {
                    super.onSuccessSynchronous(obj)
                    Log.d(TAG, "PluginHandleSendMessageCallbacks.onSuccessSynchronous $obj")
                }

                override fun onSuccesAsynchronous() {
                    super.onSuccesAsynchronous()
                    Log.d(TAG, "PluginHandleSendMessageCallbacks.onSuccessAsynchronous")
                }

                override fun getMessage(): JSONObject {
                    Log.d(TAG, "PluginHandleSendMessageCallbacks.getMessage")
                    return super.getMessage()
                }

                override fun onCallbackError(error: String?) {
                    super.onCallbackError(error)
                    Log.d(TAG, "PluginHandleSendMessageCallbacks: $error")
                }
            })
        }

        override fun onCallbackError(error: String?) {
            super.onCallbackError(error)
            Log.d(TAG, "createAnswer.onCallbackError: $error")
        }
    })
}

fun JanusManager.hangUp() {
    val msg = JSONObject()
    val body = JSONObject()
    body.put(CallStatus.REQUEST.status, CallStatus.HANGUP.status)
    msg.put(CallStatus.MESSAGE.status, body)
    handle?.sendMessage(PluginHandleSendMessageCallbacks(msg))
}

fun JanusManager.decline() {
    val msg = JSONObject()
    val body = JSONObject()
    body.put(CallStatus.REQUEST.status, CallStatus.DECLINE.status)
    msg.put(CallStatus.MESSAGE.status, body)
    handle?.sendMessage(PluginHandleSendMessageCallbacks(msg))
}

package com.mvine.mcomm.janus.extension

import android.util.Log
import com.mvine.janusclient.PluginHandleSendMessageCallbacks
import com.mvine.mcomm.janus.JanusManager
import org.json.JSONObject

fun JanusManager.startSIPRegistration() {
    var obj: JSONObject?= null
    personInfo?.let { personInfo ->
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
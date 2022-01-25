package com.mvine.mcomm.janus.extension

import android.util.Log
import com.mvine.janusclient.PluginHandleSendMessageCallbacks
import com.mvine.mcomm.janus.JanusManager
import com.mvine.mcomm.janus.JanusManager.Companion.TAG
import com.mvine.mcomm.janus.commonvalues.CallStatus
import org.json.JSONObject

fun JanusManager.startSIPRegistration() {
    var obj: JSONObject?
    personInfo.let { personInfo ->
        personInfo.view.apply {
            obj = JSONObject().apply {
                put(CallStatus.REQUEST.status, CallStatus.REGISTER.status)
                put("authuser", nSTX)
                put("username", "sip:$nSTX@portaluat.mvine.com")
                put("secret", nastrpass)
                put("display_name", contact?.name)
                put("proxy", "sip:portaluat.mvine.com:5068")
            }
        }
    }
    val message = JSONObject().apply {
        put(CallStatus.MESSAGE.status, obj)
    }
    handle?.sendMessage(object : PluginHandleSendMessageCallbacks(message) {
        override fun onSuccessSynchronous(obj: JSONObject?) {
            super.onSuccessSynchronous(obj)
        }

        override fun onCallbackError(error: String?) {
            super.onCallbackError(error)
            Log.d(TAG, error.toString())
        }

        override fun onSuccesAsynchronous() {
            super.onSuccesAsynchronous()
            Log.d(TAG, "onSuccesAsynchronous")
        }
    })
}

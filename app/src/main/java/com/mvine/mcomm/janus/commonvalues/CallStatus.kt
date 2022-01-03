package com.mvine.mcomm.janus.commonvalues

import androidx.annotation.StringDef

@StringDef(
    CallStatus.CREATE,
    CallStatus.REGISTER,
    CallStatus.REQUEST,
    CallStatus.CALL,
    CallStatus.MESSAGE,
    CallStatus.ACCEPT,
    CallStatus.HANGUP,
    CallStatus.DECLINE
)
@Retention(AnnotationRetention.SOURCE)
annotation class CallStatus {
    companion object {
        const val CREATE = "create"
        const val REGISTER = "register"
        const val REQUEST = "request"
        const val CALL = "call"
        const val MESSAGE = "message"
        const val ACCEPT = "accept"
        const val HANGUP = "hangup"
        const val DECLINE = "decline"
    }
}
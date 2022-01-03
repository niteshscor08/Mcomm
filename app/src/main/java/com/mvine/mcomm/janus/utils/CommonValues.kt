package com.mvine.mcomm.janus.utils

import androidx.annotation.IntDef
import androidx.annotation.StringDef

@IntDef(
    CommonIntegerValue.CREATE_KEY_LENGTH
)
@Retention(AnnotationRetention.SOURCE)
annotation class CommonIntegerValue {
    companion object{
        const val CREATE_KEY_LENGTH = 12
    }
}


@StringDef( CommonStringValues.CREATE,
    CommonStringValues.JANUS_REGISTERED,
    CommonStringValues.JANUS_REGISTRATION_FAILED,
    CommonStringValues.JANUS_ACCEPTED,
    CommonStringValues.JANUS_CALLING,
    CommonStringValues.JANUS_RINGING,
    CommonStringValues.JANUS_PROCEEDING,
    CommonStringValues.JANUS_DECLINING,
    CommonStringValues.JANUS_HANGINGUP,
    CommonStringValues.JANUS_HANGUP,
    CommonStringValues.JANUS_INCOMING_CALL,
    CommonStringValues.INCOMING,
    CommonStringValues.OUTGOING,
)
@Retention(AnnotationRetention.SOURCE)
annotation class CommonStringValues {
    companion object {
        const val CREATE = "create"
        const val JANUS_REGISTERED = "registered"
        const val JANUS_REGISTRATION_FAILED = "registration_failed"
        const val JANUS_ACCEPTED = "accepted"
        const val JANUS_CALLING = "calling"
        const val JANUS_RINGING = "ringing"
        const val JANUS_PROCEEDING = "proceeding"
        const val JANUS_DECLINING = "declining"
        const val JANUS_HANGINGUP = "hangingup"
        const val JANUS_HANGUP = "hangup"
        const val JANUS_INCOMING_CALL = "incomingcall"
        const val INCOMING = "incoming_dialog"
        const val OUTGOING = "outgoing_dialog"
    }
}
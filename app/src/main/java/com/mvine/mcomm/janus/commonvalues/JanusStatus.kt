package com.mvine.mcomm.janus.commonvalues

import androidx.annotation.StringDef

@StringDef(
    JanusStatus.JANUS_REGISTERED,
    JanusStatus.JANUS_REGISTRATION_FAILED,
    JanusStatus.JANUS_ACCEPTED,
    JanusStatus.JANUS_CALLING,
    JanusStatus.JANUS_RINGING,
    JanusStatus.JANUS_PROCEEDING,
    JanusStatus.JANUS_DECLINING,
    JanusStatus.JANUS_HANGINGUP,
    JanusStatus.JANUS_HANGUP,
    JanusStatus.JANUS_INCOMING_CALL
)
@Retention(AnnotationRetention.SOURCE)
annotation class JanusStatus {
    companion object {
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
    }
}

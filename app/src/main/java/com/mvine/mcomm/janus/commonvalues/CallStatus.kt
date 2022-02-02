package com.mvine.mcomm.janus.commonvalues

enum class CallStatus(
    val status: String
) {

    CREATE("create"),

    REGISTERED("registered"),

    REGISTERING("registering"),

    REGISTER("register"),

    REGISTRATION_FAILED("registration_failed"),

    ACCEPT("accept"),

    ACCEPTED("accepted"),

    CALL("call"),

    CALLING("calling"),

    RINGING("ringing"),

    PROCEEDING("proceeding"),

    DECLINING("declining"),

    HANGINGUP("hangingup"),

    HANGUP("hangup"),

    INCOMING_CALL("incomingcall"),

    REQUEST("request"),

    MESSAGE("message"),

    DECLINE("decline");
}

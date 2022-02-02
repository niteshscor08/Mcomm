package com.mvine.mcomm.janus.extension

fun String.toSIPRemoteAddress(): String {
    return "sip:$this@portaluat.mvine.com:5068"
}

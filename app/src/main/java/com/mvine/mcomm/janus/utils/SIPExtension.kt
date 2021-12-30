package com.mvine.mcomm.janus.utils

fun String.toSIPRemoteAddress(): String {
    return "sip:$this@portaluat.mvine.com:5068"
}
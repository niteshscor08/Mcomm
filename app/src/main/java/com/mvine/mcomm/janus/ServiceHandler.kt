package com.mvine.mcomm.janus

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class ServiceHandler(private val service: WeakReference<JanusManager>) : Handler() {
    private val TAG: String = this.javaClass.name

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val srv = service.get() ?: return
        when (msg.what) {
        }
    }
}

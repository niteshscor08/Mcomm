package com.mvine.mcomm.janus.request

import android.os.Parcelable
import com.mvine.janusclient.JanusMessageType
import com.mvine.janusclient.JanusSupportedPluginPackages
import com.mvine.janusclient.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class JanusCreate (
    val janus: String= JanusMessageType.create.toString(),
    val transaction: String?= null
): Parcelable

@Parcelize
data class JanusRegister(
    val janus: String = JanusMessageType.message.toString(),
    val session_id: Long,
    val handle_id: Long,
    val body: Body
): Parcelable{
    @Parcelize
    data class Body(
    val ptype: String = "publisher",
    var display: String? = null,
    val request: String = "register",
    val proxy: String = "sip:portaluat.mvine.com:5068",
    var authuser: String? = null,
    var username: String? = null,
    var display_name: String? = null,
    var secret: String? = null
    ): Parcelable
}

@Parcelize
data class JanusAttach(
    val id: Long? = null,
    val transaction: String = TransactionType.attach.toString(),
    val plugin: String = JanusSupportedPluginPackages.JANUS_SIP.toString()
    ): Parcelable




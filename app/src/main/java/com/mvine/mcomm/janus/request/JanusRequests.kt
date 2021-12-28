package com.mvine.mcomm.janus.request

import android.os.Parcelable
import com.mvine.janusclient.JanusMessageType
import com.mvine.janusclient.JanusSupportedPluginPackages
import com.mvine.janusclient.TransactionType
import com.mvine.mcomm.janus.utils.CommonValues
import com.mvine.mcomm.janus.utils.RandomString
import kotlinx.parcelize.Parcelize

@Parcelize
data class JanusCreate (
    val janus: String= JanusMessageType.create.toString(),
    val transaction: String = RandomString().randomString(CommonValues.CREATE_KEY_LENGTH)
): Parcelable

@Parcelize
data class JanusRegister(
    //val janus: String = JanusMessageType.message.toString(),
    //val session_id: Long,
   // val handle_id: Long,
    val body: Body
): Parcelable{
    @Parcelize
    data class Body(
    val request: String = "register",
    val proxy: String = "sip:portaluat.mvine.com:5068",
    var authuser: String,
    var username: String,
    var display_name: String,
    var secret: String
    ): Parcelable
}

@Parcelize
data class JanusAttach(
    val id: Long,
    val transaction: String = TransactionType.attach.toString(),
    val plugin: String = JanusSupportedPluginPackages.JANUS_SIP.toString()
    ): Parcelable




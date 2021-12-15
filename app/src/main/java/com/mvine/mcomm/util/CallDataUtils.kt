package com.mvine.mcomm.util

import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.model.SpinnerItem

fun getSpinnerItems(callData: CallData): ArrayList<SpinnerItem> {
    return arrayListOf(
        SpinnerItem(callImageSrc = getCallIcon(callData.type), itemText = callData.timestamp ?: ""),
        SpinnerItem(
            callImageSrc = R.drawable.ic_incoming,
            itemText = "20/05/2021 17:55 | 00:00:02 | Incoming"
        ),
        SpinnerItem(
            callImageSrc = R.drawable.ic_icon_play,
            itemText = "20/08/2021 17:55 | 00:00:02 | Voicemail"
        ),
        SpinnerItem(
            callImageSrc = R.mipmap.ic_chat,
            itemText = "29/08/2021 12:55 | Are you available for.."
        ),
        SpinnerItem(
            callImageSrc = R.drawable.ic_missed_white,
            itemText = "22/06/2021 17:55 | 00:00:00 | Missed"
        )
    )
}

private fun getCallIcon(callType: String?): Int {
    return when (callType) {
        CallType.INCOMING.type -> {
            R.drawable.ic_incoming
        }
        CallType.OUTGOING.type -> {
            R.drawable.ic_outgoing
        }
        else -> R.drawable.ic_incoming
    }
}

enum class CallType(val type: String) {
    INCOMING("IN"),
    OUTGOING("OUT")
}
package com.mvine.mcomm.util

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.model.SpinnerItem
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.RowType
import com.mvine.mcomm.presentation.common.dialog.CallDialog
import com.mvine.mcomm.presentation.common.dialog.CallDialogData
import com.mvine.mcomm.presentation.common.viewtypes.AllCallDataRowType
import com.mvine.mcomm.presentation.common.viewtypes.CallDataRowType
import com.mvine.mcomm.presentation.common.viewtypes.CallSpinnerRowType

fun getSpinnerItems(): ArrayList<SpinnerItem> {
    return arrayListOf(
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

fun getCallIcon(callType: String?): Int {
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

fun prepareRowTypesFromCallData(
    callData: ArrayList<CallData>,
    interaction: ListInteraction<CallData>
): ArrayList<RowType> {
    val rowTypes = arrayListOf<RowType>()
    callData.forEach { callDataItem ->
        callDataItem.isExpanded = false // Set Default Expanded as false
        rowTypes.add(CallDataRowType(callDataItem, interaction))
    }
    return rowTypes
}

fun prepareRowTypesFromAllCallData(
    callData: ArrayList<CallData>,
    interaction: ListInteraction<CallData>
): ArrayList<RowType> {
    val rowTypes = arrayListOf<RowType>()
    callData.forEach { callDataItem ->
        callDataItem.isExpanded = false // Set Default Expanded as false
        rowTypes.add(AllCallDataRowType(callDataItem, interaction))
    }
    return rowTypes
}

fun prepareHistoryRowTypesFromCallData(historyData: ArrayList<SpinnerItem>): ArrayList<RowType> {
    val rowTypes = arrayListOf<RowType>()
    historyData.forEach { callDataItem ->
        rowTypes.add(CallSpinnerRowType(callDataItem))
    }
    return rowTypes
}

enum class CallType(val type: String) {
    INCOMING("IN"),
    OUTGOING("OUT")
}
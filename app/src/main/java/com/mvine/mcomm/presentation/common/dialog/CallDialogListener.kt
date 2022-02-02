package com.mvine.mcomm.presentation.common.dialog

interface CallDialogListener {
    fun onCallReceived()
    fun onCallEnded(dialogType: String)
}

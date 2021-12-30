package com.mvine.mcomm.presentation.common.dialog

interface CallDialogListener {
    fun onCallButtonClick()
    fun onCancelCallButtonClick(dialogType: String)
}
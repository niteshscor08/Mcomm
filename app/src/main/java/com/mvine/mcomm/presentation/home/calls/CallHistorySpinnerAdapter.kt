package com.mvine.mcomm.presentation.home.calls

import android.app.Activity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.CallData

class CallHistorySpinnerAdapter(context: Activity): ArrayAdapter<CallData>(context, R.layout.item_spinner_call) {

    private val layoutInflater: LayoutInflater = context.layoutInflater

}
package com.mvine.mcomm.presentation.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.DialogCallBinding

class CallDialog(private val callDialogListener: CallDialogListener?,
                 private val callDialogData: CallDialogData) : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogBaseBinding = DataBindingUtil.inflate<DialogCallBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_call,
            container,
            true
        )
       callDialogData.callerName?.let {
           dialogBaseBinding.dialogCallerName.text = it
       }
       callDialogData.isIncomingDialog?.let {
           dialogBaseBinding.dialogCall.isVisible = it
           dialogBaseBinding.dialogTitle.text = if(it){ context?.getString(R.string.incoming_call)} else{ context?.getString(R.string.outgoing_call) }
       }

       dialogBaseBinding.dialogCall.setOnClickListener {
           callDialogListener?.onCallButtonClick()
       }
       dialogBaseBinding.dialogCallEnd.setOnClickListener {
           callDialogListener?.onCancelCallButtonClick()
       }
        isCancelable = false
        return dialogBaseBinding.root
    }
}
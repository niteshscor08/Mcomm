package com.mvine.mcomm.presentation.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.DialogCallBinding
import com.mvine.mcomm.domain.model.CallState


class CallDialog(private val callDialogListener: CallDialogListener,
                 private val callDialogData: CallDialogData,
                private val dialogType: String,
                private val callState: CallState) : DialogFragment() {

    private lateinit var dialogBaseBinding: DialogCallBinding

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * DIALOG_WIDTH).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         dialogBaseBinding = DataBindingUtil.inflate<DialogCallBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_call,
            container,
            true
        )
        setUp()
        isCancelable = false
        return dialogBaseBinding.root
    }

    private fun setUp() {
        setDialogTypeAndUi()
        setUiData()
        onButtonClickEvent()
    }

    private fun setDialogTypeAndUi(){
        callDialogData.callerName?.let {
            dialogBaseBinding.dialogCallerName.text = it
        }
        callDialogData.isIncomingDialog?.let {
            dialogBaseBinding.dialogCall.isVisible = it
            dialogBaseBinding.dialogTitle.text = if(it){ context?.getString(R.string.incoming_call)} else{ context?.getString(R.string.outgoing_call) }
        }
    }

    private fun setUiData(){
        callState.remoteDisplayName?.let {
            dialogBaseBinding.dialogCallerName.text = it
        }
        callState.remoteUrl?.let {
            val url = "${BuildConfig.BASE_URL}$it"
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
            Glide.with(this).load(url).apply(options).into(dialogBaseBinding.dialogTitleImg)
        }
    }

    private fun onButtonClickEvent(){
        dialogBaseBinding.dialogCall.setOnClickListener {
            callDialogListener?.onCallReceived()
        }
        dialogBaseBinding.dialogCallEnd.setOnClickListener {
            callDialogListener?.onCallEnded(dialogType)
        }
    }

    companion object{
        const val DIALOG_WIDTH = 0.80
    }
}
package com.mvine.mcomm.presentation.common.viewholderfactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvine.mcomm.R
import com.mvine.mcomm.util.Row
import com.mvine.mcomm.util.Row.CallDataRowType
import com.mvine.mcomm.util.Row.InvalidRowType
import com.mvine.mcomm.util.Row.CallSpinnerRowType
import com.mvine.mcomm.util.getRowTypeInstance

/**
 * A Factory class that helps with returning different View Holders (In case of Multiple
 * Row Type Recycler view)
 */

object ViewHolderFactory {

    class CallDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callProfileImage: ImageView? = itemView.findViewById(R.id.iv_profilePic_call)
        val callName: TextView? = itemView.findViewById(R.id.tv_name_call)
        val lastCallStatus: ImageView? = itemView.findViewById(R.id.iv_call_status)
        val lastCallHistory: TextView? = itemView.findViewById(R.id.tv_call_history)
        val callDropDown : ImageView? = itemView.findViewById(R.id.iv_call_dropDown)
        val voiceCall: ImageView? = itemView.findViewById(R.id.iv_voiceCall_call)
    }

    class CallSpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callHistoryTextView: TextView? = itemView.findViewById(R.id.tv_spinner_call_history)
        val callHistoryImageView: ImageView? = itemView.findViewById(R.id.iv_spinner_call_status)
    }

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val contactProfile: ImageView? = itemView.findViewById(R.id.iv_profilePic_contact)
        val videoCallContact: ImageView? = itemView.findViewById(R.id.iv_videoCall_contact)
        val voiceCallContact: ImageView? = itemView.findViewById(R.id.iv_voiceCall_contact)
        val nameContact: TextView? = itemView.findViewById(R.id.tv_name_contact)
        val layoutContact: LinearLayout? = itemView.findViewById(R.id.layout_contact)
        val ivMoreContacts: ImageView? = itemView.findViewById(R.id.iv_more_contacts)
    }

    /**
     * @return the Respective ViewHolder by taking the [parent] and
     * [viewType] as parameters
     */

    fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (val rowType = getRowTypeInstance(viewType)) {
            is CallDataRowType -> {
                val callDataVIew: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_call, parent, false)
                CallDataViewHolder(callDataVIew)
            }
            is Row.AllCallDataRowType -> {
                val callDataVIew: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_call, parent, false)
                CallDataViewHolder(callDataVIew)
            }
            is CallSpinnerRowType -> {
                val callSpinnerView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_spinner_call, parent, false)
                CallSpinnerViewHolder(callSpinnerView)
            }
            is Row.ContactRowType -> {
                val contactView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact, parent, false)
                ContactViewHolder(contactView)
            }
            is InvalidRowType -> throw ClassNotFoundException(rowType.errorMessage)
        }
    }
}

package com.mvine.mcomm.presentation.common.viewtypes

import androidx.recyclerview.widget.RecyclerView
import com.mvine.mcomm.domain.model.SpinnerItem
import com.mvine.mcomm.presentation.common.RowType
import com.mvine.mcomm.util.CALL_HISTORY_ROW_TYPE
import com.mvine.mcomm.presentation.common.viewholderfactory.ViewHolderFactory


data class CallSpinnerRowType(
    val spinnerItem: SpinnerItem
) : RowType {

    override fun getItemViewType() = CALL_HISTORY_ROW_TYPE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val rowViewHolder = viewHolder as ViewHolderFactory.CallSpinnerViewHolder
        rowViewHolder.apply {
            callHistoryTextView?.text = spinnerItem.itemText
            callHistoryImageView?.setImageResource(spinnerItem.callImageSrc)
        }
    }
}

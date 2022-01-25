package com.mvine.mcomm.presentation.common.viewtypes

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.RowType
import com.mvine.mcomm.presentation.common.glide.GlideApp
import com.mvine.mcomm.presentation.common.viewholderfactory.ViewHolderFactory
import com.mvine.mcomm.util.ALL_CALL_DATA_ROW_TYPE

data class AllCallDataRowType(
    val callData: CallData,
    val interaction: ListInteraction<CallData>
) : RowType {

    override fun getItemViewType() = ALL_CALL_DATA_ROW_TYPE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val rowViewHolder = viewHolder as ViewHolderFactory.CallDataViewHolder
        rowViewHolder.apply {
            callName?.text = callData.othercaller_department
            lastCallHistory?.text = callData.othercaller_stx
            lastCallStatus?.visibility = View.GONE
            callData.image_src?.let {
                loadImageUsingGlide("${BuildConfig.BASE_URL}$it", callProfileImage)
            }
            voiceCall?.setOnClickListener {
                interaction.onVoiceCallSelected(callData)
            }
            itemView.setBackgroundResource(if (callData.isExpanded) R.color.mcomm_blue else R.color.mcomm_blue_light_tint)
            callDropDown?.setImageResource(if (callData.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            itemView.setOnClickListener {
                interaction.onItemSelectedForExpansion(position, callData, isExpanded = !callData.isExpanded)
            }
        }
    }

    private fun loadImageUsingGlide(imageURL: String, imageView: ImageView?) {
        imageView?.let {
            GlideApp.with(imageView.context)
                .load(imageURL)
                .circleCrop()
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(imageView)
        }
    }
}

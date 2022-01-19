package com.mvine.mcomm.presentation.common.viewtypes

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.RowType
import com.mvine.mcomm.presentation.common.viewholderfactory.ViewHolderFactory
import com.mvine.mcomm.util.CONTACT_ROW_TYPE


data class ContactRowType(
    val contactsData: ContactsData,
    val interaction: ListInteraction<ContactsData>
) : RowType {

    override fun getItemViewType() = CONTACT_ROW_TYPE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val rowViewHolder = viewHolder as ViewHolderFactory.ContactViewHolder
        rowViewHolder.apply {
            layoutContact?.isVisible = contactsData.isExpanded
            nameContact?.isVisible = !contactsData.isExpanded
            contactsData.image_src?.let {
                loadImageUsingGlide("${BuildConfig.BASE_URL}$it", contactProfile)
            }
            nameContact?.text = contactsData.username
            ivMoreContacts?.setOnClickListener {
                interaction.onItemSelectedForExpansion(position, contactsData, isExpanded = !contactsData.isExpanded)
            }
            voiceCallContact?.setOnClickListener {
                interaction.onVoiceCallSelected(contactsData)
            }
        }
    }

    private fun loadImageUsingGlide(imageURL: String, imageView: ImageView?) {
        imageView?.let {
            Glide.with(imageView.context)
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

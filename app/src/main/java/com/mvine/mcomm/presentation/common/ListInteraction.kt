package com.mvine.mcomm.presentation.common

interface ListInteraction<T> {
    fun onItemSelectedForExpansion(position: Int = -1, item: T, isExpanded: Boolean = false) {}
    fun onVoiceCallSelected(item: T)
    fun onVideoCallSelected(item: T) {}
    fun onDetailImageViewed(imageUrl: String?) {}
}

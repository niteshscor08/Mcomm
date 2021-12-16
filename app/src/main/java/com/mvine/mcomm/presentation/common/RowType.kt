package com.mvine.mcomm.presentation.common

import androidx.recyclerview.widget.RecyclerView

/**
 * A Basic Interface which will be implemented for different Row items (in case of a Multiple
 * Row Recycler view)
 */

interface RowType {

    fun getItemViewType(): Int

    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int)
}

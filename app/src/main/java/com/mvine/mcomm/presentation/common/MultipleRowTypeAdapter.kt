package com.mvine.mcomm.presentation.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.util.CALL_DATA_ROW_TYPE
import com.mvine.mcomm.presentation.common.viewholderfactory.ViewHolderFactory
import com.mvine.mcomm.presentation.common.viewtypes.CallDataRowType
import com.mvine.mcomm.util.CALL_HISTORY_ROW_TYPE
import com.mvine.mcomm.util.prepareHistoryRowTypesFromCallData

/**
 * A Base Adapter class to handle Multiple Row Types in a Recycler view
 * @param dataSet The DataSet containing all the different types of [RowType] instances
 */

class MultipleRowTypeAdapter(private val dataSet: ArrayList<RowType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int = dataSet[position].getItemViewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolderFactory.create(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        dataSet[position].onBindViewHolder(holder, position)
    }

    /**
     * Updates the existing [dataSet] with the new [data]
     */
    fun updateData(data: ArrayList<RowType>) {
        this.dataSet.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun expandCallHistory(position: Int, callData: CallData) {
        val rowType = dataSet[position]
        if (rowType.getItemViewType() == CALL_DATA_ROW_TYPE) {
            (rowType as CallDataRowType).callData.let {
                if (it.isExpanded) return
                it.isExpanded = true
            }
        }
        var insertPosition = position + 1
        callData.callHistory?.let {
            prepareHistoryRowTypesFromCallData(it).forEach { rowType ->
                dataSet.add(insertPosition++, rowType)
            }
        }
        notifyDataSetChanged()
    }

    fun dissolveCallHistory(position: Int, callData: CallData) {
        val rowType = dataSet[position]
        if (rowType.getItemViewType() == CALL_DATA_ROW_TYPE) {
            (rowType as CallDataRowType).callData.let {
                if (!it.isExpanded) return
                it.isExpanded = false
            }
        }
        val deletePosition = position + 1
        if (deletePosition >= dataSet.size) return
        var historyRowType = dataSet[deletePosition]
        while (historyRowType.getItemViewType() == CALL_HISTORY_ROW_TYPE) {
            dataSet.removeAt(deletePosition)
            if (deletePosition >= dataSet.size) break
            historyRowType = dataSet[deletePosition]
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size
}
package com.mvine.mcomm.presentation.home.calls

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mvine.mcomm.R
import com.mvine.mcomm.domain.model.SpinnerItem

class CallHistorySpinnerAdapter(
    private val activityContext: Activity,
    private val resourceId: Int,
    private val items: ArrayList<SpinnerItem>
) : ArrayAdapter<SpinnerItem>(activityContext, resourceId, items) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = activityContext.layoutInflater
        val itemView = inflater.inflate(R.layout.item_spinner_call, parent, false)
        itemView.apply {
            val spinnerText = findViewById<TextView>(R.id.tv_spinner_call_history)
            val callImageStart = findViewById<ImageView>(R.id.iv_spinner_call_status_start)
            val callImageEnd = findViewById<ImageView>(R.id.iv_spinner_call_status_end)
            callImageStart.apply {
                visibility = if (position == 0) View.VISIBLE else View.GONE
                setImageResource(items[position].callImageSrc)

            }
            callImageEnd.apply {
                visibility = if (position == 0) View.GONE else View.VISIBLE
                setImageResource(items[position].callImageSrc)
            }
            spinnerText.text = items[position].itemText
        }
        return itemView
    }

    fun updateItems(items: ArrayList<SpinnerItem>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

}
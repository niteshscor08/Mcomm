package com.mvine.mcomm.presentation.home.calls

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvine.mcomm.BuildConfig
import com.mvine.mcomm.R
import com.mvine.mcomm.databinding.ItemCallBinding
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.util.getSpinnerItems

class CallsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemCallBinding: ItemCallBinding

    private var callHistorySpinnerAdapter: CallHistorySpinnerAdapter? = null

    private val diffCallback = object : DiffUtil.ItemCallback<CallData>() {

        override fun areItemsTheSame(oldItem: CallData, newItem: CallData): Boolean =
            oldItem.othercaller_stx == newItem.othercaller_stx

        override fun areContentsTheSame(oldItem: CallData, newItem: CallData): Boolean =
            oldItem == newItem

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        itemCallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_call,
            parent,
            false
        )
        return NewsArticleViewHolder(
            itemCallBinding.root,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsArticleViewHolder -> {
                holder.bind(itemCallBinding, differ.currentList[position], callHistorySpinnerAdapter)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CallData>) {
        differ.submitList(list)
    }

    fun setSpinnerAdapterInstance(callHistorySpinnerAdapter: CallHistorySpinnerAdapter) {
        this.callHistorySpinnerAdapter = callHistorySpinnerAdapter
    }

    class NewsArticleViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            binding: ItemCallBinding,
            item: CallData,
            callHistorySpinnerAdapter: CallHistorySpinnerAdapter?
        ) = with(itemView) {
            binding.ivVoiceCallCall.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            binding.callData = item
            callHistorySpinnerAdapter?.let {
                val spinnerItems = getSpinnerItems(item)
                it.updateItems(spinnerItems)
                binding.spinnerHistoryCall.adapter = it
            }
            item.image_src?.let { url ->
                loadImageUsingGlide(binding.ivProfilePicCall, "${BuildConfig.BASE_URL}$url")
            }
        }

        private fun loadImageUsingGlide(imageView: ImageView, avatarURL: String) {
            Glide.with(imageView.context)
                .load(avatarURL)
                .circleCrop()
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(imageView)
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CallData)
    }
}
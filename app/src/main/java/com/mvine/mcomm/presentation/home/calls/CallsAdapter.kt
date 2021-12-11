package com.mvine.mcomm.presentation.home.calls

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

class CallsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemCallBinding: ItemCallBinding

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
                holder.bind(itemCallBinding, differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CallData>) {
        differ.submitList(list)
    }

    class NewsArticleViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(binding: ItemCallBinding, item: CallData) = with(itemView) {
            setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            binding.callData = item
            /**
             * Spinner code set up
             * TODO uncomment once resolved
             */
//            ArrayAdapter.createFromResource(
//                context,
//                R.array.planets_array,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(R.layout.item_spinner_call)
//                // Apply the adapter to the spinner
//                binding.spinnerHistoryCall.adapter = adapter
//            }
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
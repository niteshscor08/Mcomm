package com.mvine.mcomm.presentation.home.contacts

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
import com.mvine.mcomm.databinding.ItemContactBinding
import com.mvine.mcomm.domain.model.ContactsData

class ContactsAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemContactBinding: ItemContactBinding

    private val diffCallback = object : DiffUtil.ItemCallback<ContactsData>() {

        override fun areItemsTheSame(oldItem: ContactsData, newItem: ContactsData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ContactsData, newItem: ContactsData): Boolean =
            oldItem == newItem

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        itemContactBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_contact,
            parent,
            false
        )
        return NewsArticleViewHolder(
            itemContactBinding.root,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsArticleViewHolder -> {
                holder.bind(itemContactBinding, differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ContactsData>) {
        differ.submitList(list)
    }

    class NewsArticleViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(binding: ItemContactBinding, item: ContactsData) = with(itemView) {
            setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            binding.contactData = item
            item.image_src?.let { url ->
                loadImageUsingGlide(binding.ivProfilePicContact, "${BuildConfig.BASE_URL}$url")
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
        fun onItemSelected(position: Int, item: ContactsData)
    }
}
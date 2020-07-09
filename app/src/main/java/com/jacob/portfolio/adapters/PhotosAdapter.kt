package com.jacob.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.jacob.portfolio.R
import com.jacob.portfolio.models.PhotoItem
import kotlinx.android.synthetic.main.item_photo.view.*


class PhotosAdapter (private var list: List<PhotoItem>,
                     private val listener : OnItemClickListener,
                     private val context : Context) : RecyclerView.Adapter<PhotosAdapter.ItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(photoItem: PhotoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_photo,
            parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]

        val url = GlideUrl(
            item.thumbnailUrl, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount() = list.size

    fun setItems(newList: List<PhotoItem>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.item_photo_iv

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(list[position])
            }
        }
    }
}
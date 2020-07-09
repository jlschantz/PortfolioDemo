package com.jacob.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacob.portfolio.R
import com.jacob.portfolio.models.AlbumItem
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumsAdapter (private var list: List<AlbumItem>,
                     private val listener : OnItemClickListener,
                     private val context : Context) : RecyclerView.Adapter<AlbumsAdapter.ItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(albumItem: AlbumItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_album,
            parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.textView1.text = item.title
    }

    override fun getItemCount() = list.size

    fun setItems(newList: List<AlbumItem>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textView1: TextView = itemView.item_album_tv_top

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
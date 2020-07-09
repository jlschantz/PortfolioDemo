package com.jacob.portfolio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacob.portfolio.R
import com.jacob.portfolio.models.UserItem
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter (private var list: List<UserItem>,
                    private val listener : OnItemClickListener,
                    private val context : Context) : RecyclerView.Adapter<UsersAdapter.ItemViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(userItem: UserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_user,
            parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.textView1.text = item.name
    }

    override fun getItemCount() = list.size

    fun setItems(newList: List<UserItem>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textView1: TextView = itemView.item_user_tv_top

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
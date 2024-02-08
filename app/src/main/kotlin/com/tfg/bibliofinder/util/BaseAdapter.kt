package com.tfg.bibliofinder.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R

abstract class BaseAdapter<T>(
    private val items: List<T>,
    private val clickListener: ItemClickListener<T>?,
    private val layoutResId: Int
) : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {

    private val isFrontMap: MutableMap<Int, Boolean> = mutableMapOf()

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        for (i in items.indices) {
            isFrontMap[i] = true
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        bindItem(holder.itemView, item)

        clickListener?.let { listener ->
            holder.itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }

        val libraryInfoButton: Button? = holder.itemView.findViewById(R.id.library_info)
        val bookButton: Button? = holder.itemView.findViewById(R.id.book_button)

        libraryInfoButton?.setOnClickListener {
            clickListener?.onInfoButtonClick(item)
        }

        bookButton?.setOnClickListener {
            clickListener?.onBookButtonClick(item)
        }
    }

    abstract fun bindItem(view: View, item: T)
}
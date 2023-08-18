package com.tfg.bibliofinder.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R

abstract class BaseAdapter<T>(
    private val items: List<T>,
    private val onItemClick: ((T) -> Unit)? = null,
    private val onActionButtonClicked: ((T) -> Unit)? = null,
    private val layoutResId: Int
) : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        bindItem(holder.itemView, item)

        onItemClick?.let { click ->
            holder.itemView.setOnClickListener {
                click(item)
            }
        }

        val libraryInfoButton: ImageButton? = holder.itemView.findViewById(R.id.library_info)

        libraryInfoButton?.setOnClickListener {
            // Mostrar información detallada de la biblioteca relacionada con el ítem
            // onActionButtonClicked?.invoke(item)
        }

        val bookButton: ImageButton? = holder.itemView.findViewById(R.id.book_button)

        bookButton?.setOnClickListener {
            onActionButtonClicked?.invoke(item)
        }
    }

    abstract fun bindItem(view: View, item: T)
}
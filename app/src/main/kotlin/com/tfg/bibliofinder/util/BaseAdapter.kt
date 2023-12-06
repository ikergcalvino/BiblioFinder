package com.tfg.bibliofinder.util

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R

abstract class BaseAdapter<T>(
    private val items: List<T>,
    private val onItemClick: ((T) -> Unit)? = null,
    private val onActionButtonClicked: ((T) -> Unit)? = null,
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

        onItemClick?.let { click ->
            holder.itemView.setOnClickListener {
                click(item)
            }
        }

        val libraryInfoButton: ImageButton? = holder.itemView.findViewById(R.id.library_info)
        val closeInfoButton: ImageButton? = holder.itemView.findViewById(R.id.close_info)
        val bookButton: ImageButton? = holder.itemView.findViewById(R.id.book_button)

        libraryInfoButton?.setOnClickListener {
            toggleCard(holder)
        }

        closeInfoButton?.setOnClickListener {
            toggleCard(holder)
        }

        bookButton?.setOnClickListener {
            onActionButtonClicked?.invoke(item)
        }
    }

    private fun toggleCard(holder: BaseViewHolder) {
        val currentPosition = holder.adapterPosition
        val isFront = isFrontMap[currentPosition] ?: true

        val flipOutAnimator = AnimatorInflater.loadAnimator(
            holder.itemView.context, R.animator.flip_out
        ) as AnimatorSet
        val flipInAnimator = AnimatorInflater.loadAnimator(
            holder.itemView.context, R.animator.flip_in
        ) as AnimatorSet

        val cardFront = holder.itemView.findViewById<CardView>(R.id.library_card_front)
        val cardBack = holder.itemView.findViewById<CardView>(R.id.library_card_back)

        if (isFront) {
            flipOutAnimator.setTarget(holder.itemView.findViewById(R.id.library_card_front))
            flipInAnimator.setTarget(holder.itemView.findViewById(R.id.library_card_back))
            cardFront.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
        } else {
            flipOutAnimator.setTarget(holder.itemView.findViewById(R.id.library_card_back))
            flipInAnimator.setTarget(holder.itemView.findViewById(R.id.library_card_front))
            cardBack.visibility = View.GONE
            cardFront.visibility = View.VISIBLE
        }

        flipOutAnimator.start()
        flipInAnimator.start()

        isFrontMap[currentPosition] = !isFront
    }

    abstract fun bindItem(view: View, item: T)
}
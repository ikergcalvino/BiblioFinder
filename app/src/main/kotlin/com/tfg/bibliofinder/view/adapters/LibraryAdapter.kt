package com.tfg.bibliofinder.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Library

class LibraryAdapter(
    private val libraries: List<Library>, private val onItemClick: (Library) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    inner class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var libraryName: TextView = itemView.findViewById(R.id.library_name)
        var librarySchedule: TextView = itemView.findViewById(R.id.library_schedule)
        var libraryCapacity: TextView = itemView.findViewById(R.id.library_capacity)
    }

    override fun getItemCount(): Int {
        return libraries.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LibraryViewHolder {
        val v: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_library, viewGroup, false)
        return LibraryViewHolder(v)
    }

    override fun onBindViewHolder(libraryViewHolder: LibraryViewHolder, idx: Int) {
        val library = libraries[idx]

        libraryViewHolder.libraryName.text = library.name

        val scheduleText = libraryViewHolder.itemView.context.getString(
            R.string.schedule_format, library.openingTime, library.closingTime
        )
        libraryViewHolder.librarySchedule.text = scheduleText

        val capacityText = libraryViewHolder.itemView.context.getString(
            R.string.capacity_format, library.capacity
        )
        libraryViewHolder.libraryCapacity.text = capacityText

        libraryViewHolder.itemView.setOnClickListener {
            onItemClick(library)
        }
    }
}
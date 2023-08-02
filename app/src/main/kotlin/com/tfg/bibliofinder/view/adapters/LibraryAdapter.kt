package com.tfg.bibliofinder.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.Library

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
            LayoutInflater.from(viewGroup.context).inflate(R.layout.library_card, viewGroup, false)
        return LibraryViewHolder(v)
    }

    override fun onBindViewHolder(libraryViewHolder: LibraryViewHolder, idx: Int) {
        val library = libraries[idx]
        libraryViewHolder.libraryName.text = library.name
        libraryViewHolder.librarySchedule.text = library.schedule
        libraryViewHolder.libraryCapacity.text = "Capacity: ${library.capacity}"

        libraryViewHolder.itemView.setOnClickListener {
            onItemClick(library)
        }
    }
}

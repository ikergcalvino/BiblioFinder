package com.tfg.bibliofinder.view.adapters

import android.view.View
import android.widget.TextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Library

class LibraryAdapter(
    libraries: List<Library>, onItemClick: (Library) -> Unit
) : BaseAdapter<Library>(libraries, onItemClick, null, R.layout.card_library) {

    override fun bindItem(view: View, item: Library) {
        val libraryName: TextView = view.findViewById(R.id.library_name)
        val librarySchedule: TextView = view.findViewById(R.id.library_schedule)
        val libraryCapacity: TextView = view.findViewById(R.id.library_capacity)

        libraryName.text = item.name
        val scheduleText = view.context.getString(
            R.string.schedule_format, item.openingTime, item.closingTime
        )
        librarySchedule.text = scheduleText
        val capacityText = view.context.getString(
            R.string.capacity_format, item.capacity
        )
        libraryCapacity.text = capacityText
    }
}
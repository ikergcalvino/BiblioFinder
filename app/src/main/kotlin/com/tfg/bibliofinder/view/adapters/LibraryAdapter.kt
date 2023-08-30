package com.tfg.bibliofinder.view.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.model.entities.Library

class LibraryAdapter(
    libraries: List<Library>, onItemClick: (Library) -> Unit
) : BaseAdapter<Library>(libraries, onItemClick, null, R.layout.card_library) {

    override fun bindItem(view: View, item: Library) {
        val libraryName: TextView = view.findViewById(R.id.library_name)
        libraryName.text = item.name

        val librarySchedule: TextView = view.findViewById(R.id.library_schedule)
        librarySchedule.text = view.context.getString(
            R.string.schedule_format, item.openingTime, item.closingTime
        )

        val libraryFreeSpaces: TextView = view.findViewById(R.id.library_free_spaces)
        libraryFreeSpaces.text = view.context.getString(
            R.string.free_spaces_format, item.capacity
        )

        val iconAdapted: ImageView = view.findViewById(R.id.library_isAdapted)
        if (item.isAdapted) {
            iconAdapted.visibility = View.VISIBLE
        } else {
            iconAdapted.visibility = View.INVISIBLE
        }

        val libraryAddress: TextView = view.findViewById(R.id.library_address)
        libraryAddress.text = item.address

        val libraryPhone: TextView = view.findViewById(R.id.library_phone)
        libraryPhone.text = item.phone

        val libraryEmail: TextView = view.findViewById(R.id.library_email)
        libraryEmail.text = item.email
    }
}
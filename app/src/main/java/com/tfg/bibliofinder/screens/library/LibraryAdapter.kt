package com.tfg.bibliofinder.screens.library

import android.view.View
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardLibraryBinding
import com.tfg.bibliofinder.entities.Library
import com.tfg.bibliofinder.util.BaseAdapter
import com.tfg.bibliofinder.util.ItemClickListener

class LibraryAdapter(
    libraries: List<Library>, clickListener: ItemClickListener<Library>
) : BaseAdapter<Library>(libraries, clickListener, R.layout.card_library) {

    override fun bindItem(view: View, item: Library) {
        val binding = CardLibraryBinding.bind(view)
        val context = view.context

        binding.apply {
            libraryName.text = item.name
            librarySchedule.text =
                context.getString(R.string.schedule_format, item.openingTime, item.closingTime)
            libraryFreeSpaces.text =
                context.getString(R.string.free_spaces_format, item.freeSpaces, item.capacity)

            libraryIsAdapted.visibility = if (item.isAdapted) View.VISIBLE else View.INVISIBLE
        }
    }
}
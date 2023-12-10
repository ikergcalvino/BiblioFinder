package com.tfg.bibliofinder.screens.library

import android.view.View
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.CardLibraryBinding
import com.tfg.bibliofinder.entities.Library
import com.tfg.bibliofinder.util.BaseAdapter

class LibraryAdapter(
    libraries: List<Library>, onItemClick: (Library) -> Unit
) : BaseAdapter<Library>(libraries, onItemClick, null, R.layout.card_library) {

    override fun bindItem(view: View, item: Library) {
        val binding = CardLibraryBinding.bind(view)
        val context = view.context

        binding.libraryName.text = item.name
        binding.librarySchedule.text = context.getString(
            R.string.schedule_format, item.openingTime, item.closingTime
        )
        binding.libraryFreeSpaces.text = context.getString(
            R.string.free_spaces_format, item.freeSpaces, item.capacity
        )

        binding.libraryIsAdapted.visibility = if (item.isAdapted) View.VISIBLE else View.INVISIBLE

        binding.libraryAddress.text = item.address
        binding.libraryPhone.text = item.phone
        binding.libraryEmail.text = item.email
        binding.libraryInstitution.text = item.institution
    }
}
package com.tfg.bibliofinder.screens.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.entities.Library

class LibraryBottomSheetFragment(private val library: Library) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_library, container, false)?.apply {
            findViewById<MaterialTextView>(R.id.library_address).text = library.address
            findViewById<MaterialTextView>(R.id.library_phone).text = library.phone
            findViewById<MaterialTextView>(R.id.library_email).text = library.email
            findViewById<MaterialTextView>(R.id.library_institution).text = library.institution
        }
    }
}
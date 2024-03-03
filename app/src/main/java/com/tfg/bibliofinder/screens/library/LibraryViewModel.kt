package com.tfg.bibliofinder.screens.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Library
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LibraryViewModel : ViewModel(), KoinComponent {

    private val database: AppDatabase by inject()

    val allLibraries: LiveData<List<Library>> = database.libraryDao().getLibraries()
}
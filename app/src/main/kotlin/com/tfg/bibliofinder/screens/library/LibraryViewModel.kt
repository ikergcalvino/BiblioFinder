package com.tfg.bibliofinder.screens.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Library

class LibraryViewModel(database: AppDatabase) : ViewModel() {

    val allLibraries: LiveData<List<Library>> = database.libraryDao().getAllLibraries()
}
package com.tfg.bibliofinder.viewmodel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Library

class LibraryViewModel(database: AppDatabase) : ViewModel() {

    val allLibraries: LiveData<List<Library>> = database.libraryDao().getAllLibrariesLiveData()

}
package com.tfg.bibliofinder.viewmodel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Classroom

class ClassroomViewModel(private val database: AppDatabase) : ViewModel() {

    fun getClassroomsInLibrary(libraryId: Long): LiveData<List<Classroom>> {
        return database.classroomDao().getClassroomsInLibrary(libraryId)
    }
}
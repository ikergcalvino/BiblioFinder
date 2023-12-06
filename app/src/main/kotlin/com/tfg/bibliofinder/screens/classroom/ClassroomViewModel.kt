package com.tfg.bibliofinder.screens.classroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Classroom

class ClassroomViewModel(private val database: AppDatabase) : ViewModel() {

    fun getClassroomsInLibrary(libraryId: Long): LiveData<List<Classroom>> {
        return database.classroomDao().getClassroomsInLibrary(libraryId)
    }
}
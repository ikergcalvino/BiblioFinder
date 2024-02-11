package com.tfg.bibliofinder.screens.classroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Classroom
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClassroomViewModel : ViewModel(), KoinComponent {
    private val database: AppDatabase by inject()

    fun getClassroomsByLibrary(libraryId: Long): LiveData<List<Classroom>> {
        return database.classroomDao().getClassroomsByLibrary(libraryId)
    }
}
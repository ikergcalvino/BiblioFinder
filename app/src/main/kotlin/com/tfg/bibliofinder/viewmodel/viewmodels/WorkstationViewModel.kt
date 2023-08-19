package com.tfg.bibliofinder.viewmodel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.model.data.local.database.AppDatabase
import com.tfg.bibliofinder.model.entities.Workstation
import kotlinx.coroutines.launch

class WorkstationViewModel(private val database: AppDatabase) : ViewModel() {

    var openingTime: String? = null
    var closingTime: String? = null

    fun getWorkstationsInClassroom(classroomId: Long): LiveData<List<Workstation>> {
        return database.workstationDao().getWorkstationsInClassroom(classroomId)
    }

    fun loadOpeningAndClosingTime(classroomId: Long) {
        viewModelScope.launch {
            val classroom = database.classroomDao().getClassroomById(classroomId)
            val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }

            openingTime = library?.openingTime
            closingTime = library?.closingTime
        }
    }

    fun reserveWorkstation(workstation: Workstation, userId: Long) {
        viewModelScope.launch {
            val updatedWorkstation =
                workstation.copy(state = Workstation.WorkstationState.BOOKED, userId = userId)

            database.workstationDao().updateWorkstation(updatedWorkstation)
        }
    }
}
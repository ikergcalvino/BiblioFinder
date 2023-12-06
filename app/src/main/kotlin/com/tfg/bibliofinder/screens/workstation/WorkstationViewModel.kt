package com.tfg.bibliofinder.screens.workstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Workstation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WorkstationViewModel : ViewModel(), KoinComponent {
    private val database: AppDatabase by inject()

    var openingTime: String? = null
    var closingTime: String? = null

    fun getWorkstationsInClassroom(classroomId: Long): LiveData<List<Workstation>> {
        return database.workstationDao().getWorkstationsInClassroom(classroomId)
    }

    suspend fun hasUserBooking(userId: Long): Boolean {
        return viewModelScope.async(Dispatchers.IO) {
            val workstation = database.workstationDao().getWorkstationByUser(userId)
            workstation != null
        }.await()
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
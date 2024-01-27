package com.tfg.bibliofinder.screens.workstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Workstation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.Date

class WorkstationViewModel : ViewModel(), KoinComponent {
    private val database: AppDatabase by inject()

    lateinit var openingTime: Date
    lateinit var closingTime: Date

    fun getWorkstationsByClassroom(classroomId: Long): LiveData<List<Workstation>> {
        return database.workstationDao().getWorkstationsByClassroom(classroomId)
    }

    suspend fun hasUserBooking(userId: Long): Boolean = withContext(Dispatchers.IO) {
        val workstation = database.workstationDao().getWorkstationByUser(userId)
        workstation != null
    }

    fun loadOpeningAndClosingTime(classroomId: Long) {
        viewModelScope.launch {
            val classroom = database.classroomDao().getClassroomById(classroomId)
            val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }

            library?.let {
                val openingHour = it.openingTime.substringBefore(":").toInt()
                val openingMinute = it.openingTime.substringAfter(":").toInt()

                openingTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, openingHour)
                    set(Calendar.MINUTE, openingMinute)
                }.time

                val closingHour = it.closingTime.substringBefore(":").toInt()
                val closingMinute = it.closingTime.substringAfter(":").toInt()

                closingTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, closingHour)
                    set(Calendar.MINUTE, closingMinute)
                }.time
            }
        }
    }

    fun reserveWorkstation(workstation: Workstation, dateTime: String, userId: Long) {
        viewModelScope.launch {
            workstation.state = Workstation.WorkstationState.BOOKED
            workstation.dateTime = dateTime
            workstation.userId = userId

            database.workstationDao().updateWorkstation(workstation)
        }
    }
}
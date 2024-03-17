package com.tfg.bibliofinder.screens.workstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.services.NotificationService
import com.tfg.bibliofinder.services.WorkstationService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime

class WorkstationViewModel : ViewModel(), KoinComponent {

    private val database: AppDatabase by inject()
    private val notificationService: NotificationService by inject()
    private val workstationService: WorkstationService by inject()

    fun getWorkstationsByClassroom(classroomId: Long): LiveData<List<Workstation>> {
        return database.workstationDao().getWorkstationsByClassroom(classroomId)
    }

    suspend fun initializeBookingsInClassroom(classroomId: Long) {
        workstationService.initializeBookingManager(classroomId)
    }

    suspend fun validateWorkstationBooking(workstation: Workstation) {
        workstationService.prebookingValidations(workstation)
    }

    suspend fun bookWorkstationAtSelectedTime(
        workstation: Workstation, bookingTime: LocalDateTime
    ) {
        workstationService.bookWorkstation(workstation, bookingTime)
        notificationService.scheduleNotification(bookingTime)
    }
}
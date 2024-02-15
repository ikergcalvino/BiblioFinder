package com.tfg.bibliofinder.screens.workstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.services.BookingService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime

class WorkstationViewModel : ViewModel(), KoinComponent {

    private val bookingService: BookingService = BookingService()
    private val database: AppDatabase by inject()

    fun getWorkstationsByClassroom(classroomId: Long): LiveData<List<Workstation>> {
        return database.workstationDao().getWorkstationsByClassroom(classroomId)
    }

    suspend fun initializeBookingsInClassroom(classroomId: Long) {
        bookingService.initializeBookingManager(classroomId)
    }

    suspend fun validateWorkstationBooking(workstation: Workstation) {
        bookingService.prebookingValidations(workstation)
    }

    suspend fun bookWorkstationAtSelectedTime(
        workstation: Workstation, selectedTime: LocalDateTime
    ) {
        bookingService.bookWorkstation(workstation, selectedTime)
    }
}
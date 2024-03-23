package com.tfg.bibliofinder.model

import android.content.SharedPreferences
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.model.exceptions.BookingOutsideAllowedHoursException
import com.tfg.bibliofinder.model.exceptions.UserAlreadyHasBookingException
import com.tfg.bibliofinder.model.exceptions.UserNotLoggedInException
import com.tfg.bibliofinder.model.exceptions.WorkstationNotAvailableException
import com.tfg.bibliofinder.util.Constants
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WorkstationManager : KoinComponent {

    private lateinit var opening: LocalDateTime
    private lateinit var closing: LocalDateTime

    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun initializeBookingManager(classroomId: Long) {
        val classroom = database.classroomDao().getClassroomById(classroomId)
        val library = classroom?.libraryId?.let { database.libraryDao().getLibraryById(it) }

        val localOpeningTime = LocalTime.parse(library?.openingTime, timeFormatter)
        val localClosingTime = LocalTime.parse(library?.closingTime, timeFormatter)

        opening = LocalDateTime.of(LocalDate.now(), localOpeningTime)
        closing = LocalDateTime.of(LocalDate.now(), localClosingTime)
    }

    suspend fun prebookingValidations(workstation: Workstation) {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)

        if (!isUserLoggedIn(userId)) throw UserNotLoggedInException()

        if (userHasBooking(userId)) throw UserAlreadyHasBookingException()

        if (!isWorkstationAvailable(workstation.state)) throw WorkstationNotAvailableException()
    }

    suspend fun bookWorkstation(
        workstation: Workstation, selectedTime: LocalDateTime
    ): LocalDateTime {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)

        if (isOutsideAllowedHours(selectedTime)) throw BookingOutsideAllowedHoursException()

        // Si la hora seleccionada ya pasó hoy, reservamos para el día siguiente
        var adjustedTime = selectedTime

        if (selectedTime.isBefore(LocalDateTime.now())) adjustedTime = selectedTime.plusDays(1)

        workstation.state = Workstation.WorkstationState.BOOKED
        workstation.dateTime = adjustedTime
        workstation.userId = userId

        database.workstationDao().updateWorkstation(workstation)

        return adjustedTime
    }

    suspend fun occupyWorkstation(nfcId: Long) {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)
        val nfcWorkstation = database.workstationDao().getWorkstationById(nfcId)
        val userWorkstation = database.workstationDao().getWorkstationByUser(userId)

        if (nfcWorkstation?.state == Workstation.WorkstationState.AVAILABLE && userWorkstation == null) {
            nfcWorkstation.state = Workstation.WorkstationState.OCCUPIED
            nfcWorkstation.userId = userId

            database.workstationDao().updateWorkstation(nfcWorkstation)
        } else if (nfcWorkstation?.state == Workstation.WorkstationState.BOOKED && nfcWorkstation.userId == userId) {
            nfcWorkstation.state = Workstation.WorkstationState.OCCUPIED

            database.workstationDao().updateWorkstation(nfcWorkstation)
        } else {
            throw WorkstationNotAvailableException()
        }
    }

    suspend fun releaseWorkstation() {
        val userId = sharedPrefs.getLong(Constants.USER_ID, 0L)
        val workstation = database.workstationDao().getWorkstationByUser(userId)

        workstation?.state = Workstation.WorkstationState.AVAILABLE
        workstation?.dateTime = null
        workstation?.userId = null

        workstation?.let { database.workstationDao().updateWorkstation(it) }
    }

    private fun isUserLoggedIn(userId: Long): Boolean = userId != 0L

    private suspend fun userHasBooking(userId: Long): Boolean =
        database.workstationDao().getWorkstationByUser(userId) != null

    private fun isWorkstationAvailable(state: Workstation.WorkstationState): Boolean =
        state == Workstation.WorkstationState.AVAILABLE

    private fun isOutsideAllowedHours(time: LocalDateTime): Boolean =
        time.isBefore(opening) || time.isAfter(closing)
}
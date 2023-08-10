package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workstation(
    @PrimaryKey(autoGenerate = true) val workstationId: Long = 0,
    val state: WorkstationState = WorkstationState.AVAILABLE,
    val date: String? = null,
    val startTime: String? = null,
    val classroomId: Long,
    val userId: Long? = null
) {
    enum class WorkstationState {
        AVAILABLE, OCCUPIED, BOOKED
    }
}
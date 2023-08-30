package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Classroom::class,
        parentColumns = ["classroomId"],
        childColumns = ["classroomId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Workstation(
    @PrimaryKey(autoGenerate = true) val workstationId: Long = 0,
    val state: WorkstationState = WorkstationState.AVAILABLE,
    var dateTime: String? = null,
    val classroomId: Long,
    val userId: Long? = null
) {
    enum class WorkstationState {
        AVAILABLE, OCCUPIED, BOOKED
    }
}
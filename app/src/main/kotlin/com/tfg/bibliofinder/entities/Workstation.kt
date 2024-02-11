package com.tfg.bibliofinder.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Classroom::class,
        parentColumns = ["classroomId"],
        childColumns = ["classroomId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("classroomId")]
)
data class Workstation(
    @PrimaryKey(autoGenerate = true) val workstationId: Long = 0,
    var state: WorkstationState = WorkstationState.AVAILABLE,
    var dateTime: LocalDateTime? = null,
    val classroomId: Long,
    var userId: Long? = null
) {
    enum class WorkstationState {
        AVAILABLE, OCCUPIED, BOOKED
    }
}
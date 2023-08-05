package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Booking(
    @PrimaryKey(autoGenerate = true) val bookingId: Long = 0,
    val date: String,
    val startTime: String,
    val endTime: String? = null,
    val userId: Long,
    val classroomId: Long
)
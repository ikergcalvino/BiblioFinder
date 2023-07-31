package com.tfg.bibliofinder.entities

data class Booking(
    val bookingId: Long,
    val date: String,
    val startTime: String,
    val endTime: String,
    val userId: Long,
    val classroomId: Long
)
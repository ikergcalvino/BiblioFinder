package com.tfg.bibliofinder.entities

data class Classroom(
    val classroomId: Long,
    val name: String,
    val capacity: Int,
    val type: String
)
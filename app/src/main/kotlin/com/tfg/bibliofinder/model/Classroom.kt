package com.tfg.bibliofinder.model

data class Classroom(
    val classroomId: Long,
    val name: String,
    val capacity: Int,
    val type: String,
    val libraryId: Long
)
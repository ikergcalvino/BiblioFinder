package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Classroom(
    @PrimaryKey(autoGenerate = true) val classroomId: Long = 0,
    val name: String,
    val capacity: Int,
    val type: String,
    val libraryId: Long
)
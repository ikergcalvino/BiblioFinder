package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workstation(
    @PrimaryKey(autoGenerate = true) val workstationId: Long = 0,
    val state: String,
    val classroomId: Long
)
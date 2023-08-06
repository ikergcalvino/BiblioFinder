package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Library(
    @PrimaryKey(autoGenerate = true) val libraryId: Long = 0,
    val name: String,
    val schedule: String,
    val capacity: Int,
    val address: String,
    val phone: String? = null,
    val email: String? = null,
    val isAdapted: Boolean,
    val type: String,
    val institution: String
)
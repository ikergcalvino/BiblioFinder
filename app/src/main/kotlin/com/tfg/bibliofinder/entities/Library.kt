package com.tfg.bibliofinder.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Library(
    @PrimaryKey(autoGenerate = true) val libraryId: Long = 0,
    val name: String,
    val openingTime: String,
    val closingTime: String,
    val capacity: Int,
    val address: String,
    val phone: String? = null,
    val email: String? = null,
    val isAdapted: Boolean,
    val institution: String
)
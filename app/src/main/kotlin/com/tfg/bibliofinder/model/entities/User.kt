package com.tfg.bibliofinder.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String? = null,
    val email: String,
    val password: String,
    val phone: String? = null
)
package com.tfg.bibliofinder.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    var name: String? = null,
    val email: String,
    val password: String,
    var phone: String? = null
)
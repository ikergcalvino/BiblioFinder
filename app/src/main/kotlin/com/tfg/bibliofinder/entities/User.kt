package com.tfg.bibliofinder.entities

data class User(
    val userId: Long,
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)
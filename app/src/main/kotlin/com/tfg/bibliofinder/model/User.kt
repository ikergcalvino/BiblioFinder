package com.tfg.bibliofinder.model

data class User(
    val userId: Long, val name: String, val email: String, val password: String, val phone: String
)
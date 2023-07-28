package com.tfg.bibliofinder.entities

data class Library(
    val libraryId: Long,
    val name: String,
    val schedule: String,
    val capacity: Int,
    val address: String,
    val phone: String,
    val email: String,
    val isAdapted: Boolean,
    val type: String,
    val institution: String
)
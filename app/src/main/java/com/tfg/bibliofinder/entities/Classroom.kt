package com.tfg.bibliofinder.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Library::class,
        parentColumns = ["libraryId"],
        childColumns = ["libraryId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("libraryId")]
)
data class Classroom(
    @PrimaryKey(autoGenerate = true) val classroomId: Long = 0,
    val name: String,
    val capacity: Int,
    var freeSpaces: Int,
    val type: String,
    val libraryId: Long
)
package com.tfg.bibliofinder.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tfg.bibliofinder.entities.Workstation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val gson = Gson()

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? = localDateTime?.format(formatter)

    @TypeConverter
    fun toLocalDateTime(string: String?): LocalDateTime? =
        string?.let { LocalDateTime.parse(it, formatter) }

    @TypeConverter
    fun fromCoordinates(coordinates: Workstation.Coordinates): String = gson.toJson(coordinates)

    @TypeConverter
    fun toCoordinates(string: String): Workstation.Coordinates =
        gson.fromJson(string, Workstation.Coordinates::class.java)
}
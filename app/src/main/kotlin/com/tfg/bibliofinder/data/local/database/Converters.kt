package com.tfg.bibliofinder.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String =
        if (value != null) value.format(formatter) else ""

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        if (!value.isNullOrBlank()) LocalDateTime.parse(value, formatter) else null
}
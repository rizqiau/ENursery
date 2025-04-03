package com.example.enursery.core.data.source.local.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let {
            LocalDate.parse(it, formatter)
        }
    }
}
package com.example.enursery.core.data.source.local.room

import androidx.room.TypeConverter
import java.time.LocalDate

class DateTypeConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }
}
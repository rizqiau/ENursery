package com.example.enursery.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val formatterIndonesia = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
    private val formatterInput = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

    fun formatTanggalIndonesia(date: LocalDate): String {
        return date.format(formatterIndonesia)
    }

    fun parseInputTanggal(text: String): LocalDate? {
        return try {
            LocalDate.parse(text, formatterInput)
        } catch (e: Exception) {
            null
        }
    }

    fun formatToInput(date: LocalDate): String {
        return date.format(formatterInput)
    }

    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    fun toEpochDay(date: LocalDate): Long = date.toEpochDay()
}
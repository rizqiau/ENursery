package com.example.enursery.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val formatterIndonesia = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
    private val formatterIndonesiaWithTime = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("id", "ID"))
    private val formatterInput = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))
    private val chartFormatter = DateTimeFormatter.ofPattern("dd/MM")

    fun formatTanggalIndonesia(date: LocalDate): String {
        return date.format(formatterIndonesia)
    }

    fun parseTanggalIndonesia(text: String): LocalDate? {
        return try {
            LocalDate.parse(text, formatterIndonesia)
        } catch (e: Exception) {
            null
        }
    }

    fun formatTanggalIndonesiaFromMillis(millis: Long): String {
        val zoneId = ZoneId.systemDefault()
        val zoneLabel = when (zoneId.id) {
            "Asia/Jakarta" -> "WIB"
            "Asia/Makassar" -> "WITA"
            "Asia/Jayapura" -> "WIT"
            else -> zoneId.id
        }

        val date = Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
        return "${date.format(formatterIndonesia)} ($zoneLabel)"
    }

    fun formatTanggalDanWaktuFromMillis(millis: Long): String {
        val zoneId = ZoneId.systemDefault()
        val zoneLabel = when (zoneId.id) {
            "Asia/Jakarta" -> "WIB"
            "Asia/Makassar" -> "WITA"
            "Asia/Jayapura" -> "WIT"
            else -> zoneId.id
        }

        val dateTime = Instant.ofEpochMilli(millis).atZone(zoneId)
        return "${dateTime.format(formatterIndonesiaWithTime)} $zoneLabel"
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

    fun fromMillisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun formatForChart(date: LocalDate): String {
        return date.format(chartFormatter)
    }
}

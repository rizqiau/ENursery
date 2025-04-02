package com.example.enursery.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    val indoFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatLocalDate(date: LocalDate?): String {
        return date?.format(indoFormatter) ?: "-"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseLocalDate(dateString: String?): LocalDate? {
        return try {
            dateString?.let { LocalDate.parse(it, indoFormatter) }
        } catch (e: Exception) {
            null
        }
    }
}

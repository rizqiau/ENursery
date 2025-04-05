package com.example.enursery.core.utils

object IdParser {
    fun extractIdPlot(idBibit: String): String? {
        val parts = idBibit.split("-")
        return if (parts.isNotEmpty()) "PLOT-${parts[0]}" else null
    }

    fun extractNamaBaris(idBibit: String): String? {
        val parts = idBibit.split("-")
        return if (parts.size >= 3) parts[1] else null
    }

    fun extractIdBaris(idBibit: String): String? {
        val parts = idBibit.split("-")
        return if (parts.size >= 3) "${parts[0]}-${parts[1]}" else null
    }
}

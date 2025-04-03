package com.example.enursery.presentation.utils

object PlotNameFormatter {

    fun formatName(input: String): String {
        val clean = input.trim().replace("plot", "", ignoreCase = true)
        return "PLOT ${clean.uppercase()}".replace(Regex("\\s+"), " ")
    }

    fun formatId(input: String): String {
        return formatName(input).replace(" ", "-")
    }
}

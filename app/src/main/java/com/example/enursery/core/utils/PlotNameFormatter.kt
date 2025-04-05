package com.example.enursery.core.utils

object PlotNameFormatter {

    fun formatName(input: String): String {
        val clean = input.trim().replace("plot", "", ignoreCase = true)
        return "PLOT ${clean.uppercase()}".replace(Regex("\\s+"), " ")
    }

    fun formatId(input: String): String {
        return formatName(input).replace(" ", "-")
    }

    fun extractKodePlot(idPlot: String): String {
        return idPlot.removePrefix("PLOT-").uppercase()
    }
}

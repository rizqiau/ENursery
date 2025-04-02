package com.example.enursery.core.domain.model

import java.time.LocalDate

data class Plot (
    val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val tanggalTanam: LocalDate,
    val tanggalTransplantasi: LocalDate,
    val varietas: String,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int
)
package com.example.enursery.core.domain.model

import java.time.LocalDate

data class Plot (
    val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val varietas: String,
    val tanggalTanam: LocalDate,
    val tanggalTransplantasi: LocalDate,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int
)
package com.example.enursery.core.domain.model

import java.util.Date

data class PlotWithVgmCountModel(
    val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val tanggalTanam: Date,
    val tanggalTransplantasi: Date,
    val varietas: String,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int,
    val jumlahVgm: Int
)


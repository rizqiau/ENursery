package com.example.enursery.core.domain.model

data class PlotWithBarisModel(
    val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val varietas: String,
    val tanggalTanam: Long,
    val tanggalTransplantasi: Long,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int,
    val barisList: List<Baris>
)

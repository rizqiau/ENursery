package com.example.enursery.core.data.source.remote.response

data class PlotResponse(
    val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val tanggalTanam: Long,
    val tanggalTransplantasi: Long,
    val varietas: String,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int
)

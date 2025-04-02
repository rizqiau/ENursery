package com.example.enursery.core.data.source.remote.response

data class VgmResponse (
    val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val idBatch: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: String,
    val latestFoto: String,
    val latestTimestamp: String
)

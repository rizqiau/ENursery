package com.example.enursery.core.domain.model

import java.util.Date

data class Vgm(
    val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: Date,
    val latestFoto: String
)

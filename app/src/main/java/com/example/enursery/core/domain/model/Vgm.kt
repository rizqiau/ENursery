package com.example.enursery.core.domain.model

import java.time.LocalDate

data class Vgm(
    val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val idBatch: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: LocalDate,
    val latestFoto: String,
    val latestTimestamp: Long
)

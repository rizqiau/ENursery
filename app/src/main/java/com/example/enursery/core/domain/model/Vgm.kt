package com.example.enursery.core.domain.model

import java.time.LocalDate

data class Vgm(
    val idBibit: String,
    val idPlot: String,
    val idUser: String?,
    val idBatch: String?,
    val idBaris: String,
    val status: String,
    val latestTinggiTanaman: Double?,
    val latestDiameterBatang: Double?,
    val latestJumlahDaun: Int?,
    val latestLebarPetiole: Double?,
    val latestTanggalInput: LocalDate?,
    val createdAt: Long?,
    val latestFoto: String?,
)

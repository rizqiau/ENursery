package com.example.enursery.core.domain.model

data class VgmWithUserModel (
    val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val idBatch: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: Long,
    val latestFoto: String,
    val latestTimestamp: Long,
    val namaUser: String
)
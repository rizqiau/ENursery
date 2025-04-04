package com.example.enursery.core.domain.model

data class VgmHistory(
    val id: String,
    val idBibit: String,
    val idPlot: String,
    val idBaris: String,
    val idUser: String,
    val idBatch: String,
    val status: String,
    val tinggi: Double,
    val diameter: Double,
    val jumlahDaun: Int,
    val tanggalInput: Long,
    val foto: String
)

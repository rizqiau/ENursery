package com.example.enursery.core.domain.model

import java.util.Date

data class VgmHistory(
    val id: String,
    val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val idBatch: String,
    val tinggi: Double,
    val diameter: Double,
    val jumlahDaun: Int,
    val tanggalInput: Date,
    val foto: String
)

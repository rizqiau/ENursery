package com.example.enursery.core.domain.model

import java.time.LocalDate

data class VgmHistory(
    val id: String,
    val idBibit: String,
    val idPlot: String,
    val idBaris: String,
    val idUser: String,
    val namaUser: String,
    val idBatch: String,
    val status: String,
    val tinggi: Double,
    val diameter: Double,
    val jumlahDaun: Int,
    val lebarPetiole: Double,
    val tanggalInput: LocalDate,
    val createdAt: Long,
    val foto: String
)

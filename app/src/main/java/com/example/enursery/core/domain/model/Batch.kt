package com.example.enursery.core.domain.model

import java.time.LocalDate

data class Batch(
    val idBatch: String,
    val namaBatch: String,
    val tanggalMulai: LocalDate,
    val tanggalSelesai: LocalDate
)

package com.example.enursery.core.domain.model

import java.util.Date

data class Batch(
    val idBatch: String,
    val namaBatch: String,
    val tanggalMulai: Date,
    val tanggalSelesai: Date
)

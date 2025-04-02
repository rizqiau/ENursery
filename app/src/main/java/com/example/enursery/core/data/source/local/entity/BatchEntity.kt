package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "batch")
data class BatchEntity(
    @PrimaryKey val idBatch: String,
    val namaBatch: String,
    val tanggalMulai: LocalDate,
    val tanggalSelesai: LocalDate
)

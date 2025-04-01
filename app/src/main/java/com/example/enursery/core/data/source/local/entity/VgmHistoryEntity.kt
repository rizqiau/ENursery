package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "vgm_history")
data class VgmHistoryEntity(
    @PrimaryKey val id: String,
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


package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vgm_history",
    foreignKeys = [
        ForeignKey(
            entity = PlotEntity::class,
            parentColumns = ["idPlot"],
            childColumns = ["idPlot"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idPlot")]
)
data class VgmHistoryEntity(
    @PrimaryKey val id: String,
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
    val tanggalInput: Long,
    val createdAt: Long,
    val foto: String
)


package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vgm",
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
data class VgmEntity (
    @PrimaryKey val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val idBaris: String,
    val idBatch: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: Long,
    val latestFoto: String,
    val latestTimestamp: Long
)
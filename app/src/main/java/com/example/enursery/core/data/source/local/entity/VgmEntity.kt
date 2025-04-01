package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "vgm")
data class VgmEntity (
    @PrimaryKey val idBibit: String,
    val idPlot: String,
    val idUser: String,
    val status: String,
    val latestTinggiTanaman: Double,
    val latestDiameterBatang: Double,
    val latestJumlahDaun: Int,
    val latestTanggalInput: Date,
    val latestFoto: String
)
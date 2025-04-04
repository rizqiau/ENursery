package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plot")
data class PlotEntity (
    @PrimaryKey val idPlot: String,
    val namaPlot: String,
    val luasArea: Double,
    val tanggalTanam: Long,
    val tanggalTransplantasi: Long,
    val varietas: String,
    val latitude: Double,
    val longitude: Double,
    val jumlahBibit: Int
)
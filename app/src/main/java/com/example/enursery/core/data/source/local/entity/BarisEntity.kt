package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "baris",
    primaryKeys = ["idBaris", "idPlot"], // composite key
    foreignKeys = [ForeignKey(
        entity = PlotEntity::class,
        parentColumns = ["idPlot"],
        childColumns = ["idPlot"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BarisEntity(
    val idBaris: String,
    val idPlot: String,
    val namaBaris: String,
    val jumlahTargetVgm: Int
)


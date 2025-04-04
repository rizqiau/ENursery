package com.example.enursery.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlotWithBaris(
    @Embedded val plot: PlotEntity,
    @Relation(
        parentColumn = "idPlot",
        entityColumn = "idPlot"
    )
    val barisList: List<BarisEntity>
)

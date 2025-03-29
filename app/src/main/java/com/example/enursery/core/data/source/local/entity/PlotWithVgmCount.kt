package com.example.enursery.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class PlotWithVgmCount(
    @Embedded val plot: PlotEntity,
    @ColumnInfo(name = "jumlahVgm") val jumlahVgm: Int
)
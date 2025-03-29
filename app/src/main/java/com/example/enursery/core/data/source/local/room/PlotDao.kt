package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount

@Dao
interface PlotDao {
    @Query("SELECT * FROM plot")
    fun getAllPlots(): LiveData<List<PlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlots(plots: List<PlotEntity>)

    @Query("""
        SELECT p.*, COUNT(v.idBibit) AS jumlahVgm
        FROM plot p
        LEFT JOIN vgm v ON p.idPlot = v.idPlot
        GROUP BY p.idPlot
    """)

    fun getPlotsWithVgmCount(): LiveData<List<PlotWithVgmCount>>
}

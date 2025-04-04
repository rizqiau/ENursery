package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithBaris
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount

@Dao
interface PlotDao {
    @Query("SELECT * FROM plot")
    fun getAllPlots(): LiveData<List<PlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlots(plots: List<PlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSinglePlot(plot: PlotEntity)

    @Query("""
        SELECT p.*, 
               IFNULL(COUNT(v.idBibit), 0) AS jumlahVgm
        FROM plot p
        LEFT JOIN vgm v ON p.idPlot = v.idPlot
        GROUP BY p.idPlot
    """)
    @Transaction
    fun getPlotsWithVgmCount(): LiveData<List<PlotWithVgmCount>>

    @Update
    suspend fun updatePlot(plot: PlotEntity)

    @Query("DELETE FROM plot WHERE idPlot = :id")
    suspend fun deletePlotById(id: String)

    @Transaction
    @Query("SELECT * FROM plot WHERE idPlot = :idPlot")
    fun getPlotWithBaris(idPlot: String): LiveData<PlotWithBaris>
}

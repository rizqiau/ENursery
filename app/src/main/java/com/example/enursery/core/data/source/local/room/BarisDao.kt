package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.enursery.core.data.source.local.entity.BarisEntity

@Dao
interface BarisDao {

    @Query("SELECT * FROM baris WHERE idPlot = :idPlot")
    fun getBarisByPlot(idPlot: String): LiveData<List<BarisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarisList(baris: List<BarisEntity>)

    @Update
    suspend fun updateBaris(baris: BarisEntity)

    @Query("DELETE FROM baris WHERE idPlot = :idPlot")
    suspend fun deleteBarisByPlot(idPlot: String)
}


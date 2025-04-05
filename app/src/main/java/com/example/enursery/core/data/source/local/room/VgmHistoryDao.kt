package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enursery.core.data.source.local.entity.VgmDailyStatEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity

@Dao
interface VgmHistoryDao {

    @Query("SELECT * FROM vgm_history WHERE idBibit = :idBibit ORDER BY tanggalInput DESC")
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVgmHistory(history: VgmHistoryEntity)

    @Query("""
    SELECT * FROM vgm_history
    WHERE (idBibit, tanggalInput) IN (
    SELECT idBibit, MAX(tanggalInput)
    FROM vgm_history
    GROUP BY idBibit
    )
    """)
    fun getLatestVgmFromHistory(): LiveData<List<VgmHistoryEntity>>

    @Query("""
    SELECT tanggalInput AS tanggal, COUNT(DISTINCT idBibit) AS jumlahInput
    FROM vgm_history
    WHERE idUser = :userId
    GROUP BY tanggalInput
    ORDER BY tanggalInput ASC
    """)
    fun getDailyInputByUser(userId: String): LiveData<List<VgmDailyStatEntity>>

}

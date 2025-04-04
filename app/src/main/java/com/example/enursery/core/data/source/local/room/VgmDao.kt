package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser

@Dao
interface VgmDao {
    @Query("SELECT * FROM vgm")
    fun getAllVgm(): LiveData<List<VgmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVgm(vgm: List<VgmEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleVgm(vgm: VgmEntity)

    @Transaction
    @Query("SELECT * FROM vgm")
    fun getAllVgmWithUser(): LiveData<List<VgmWithUser>>

    @Query("SELECT EXISTS(SELECT 1 FROM vgm WHERE idBibit = :idBibit LIMIT 1)")
    suspend fun isBibitExist(idBibit: String): Boolean
}
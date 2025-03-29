package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enursery.core.data.source.local.entity.VgmEntity

@Dao
interface VgmDao {
    @Query("SELECT * FROM vgm")
    fun getAllVgm(): LiveData<List<VgmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVgm(vgm: List<VgmEntity>)
}
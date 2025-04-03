package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.enursery.core.data.source.local.entity.BatchEntity

@Dao
interface BatchDao {

    @Query("SELECT * FROM batch")
    fun getAllBatch(): LiveData<List<BatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: List<BatchEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleBatch(batch: BatchEntity)

    @Update
    suspend fun updateBatch(batch: BatchEntity)
}

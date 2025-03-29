package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity

@Dao
interface WilayahKerjaDao {
    @Query("SELECT * FROM wilayah_kerja")
    fun getAllWilayahKerja(): LiveData<List<WilayahKerjaEntity>>

    @Query("SELECT COUNT(*) FROM wilayah_kerja")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wilayah: List<WilayahKerjaEntity>)

}

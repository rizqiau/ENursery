package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.enursery.core.data.source.local.entity.RoleEntity

@Dao
interface RoleDao {
    @Query("SELECT * FROM roles")
    fun getAllRoles(): LiveData<List<RoleEntity>>
    @Query("SELECT COUNT(*) FROM roles")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(roles: List<RoleEntity>)

}

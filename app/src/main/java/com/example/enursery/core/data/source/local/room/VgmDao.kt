package com.example.enursery.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.domain.model.VgmWithUserModel

@Dao
interface VgmDao {
    @Query("SELECT * FROM vgm")
    fun getAllVgm(): LiveData<List<VgmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVgm(vgm: List<VgmEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleVgm(vgm: VgmEntity)

    @Query("""
    SELECT vgm.*, user.namaUser AS namaUser
    FROM vgm
    INNER JOIN user ON vgm.idUser = user.idUser
""")
    fun getAllVgmWithUser(): LiveData<List<VgmWithUserModel>>

    @Transaction
    @Query("SELECT * FROM vgm")
    fun getAllVgmWithUserRel(): LiveData<List<VgmWithUser>>
}
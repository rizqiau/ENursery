package com.example.enursery.core.data.source.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.local.entity.BatchEntity
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.UserEntity
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.data.source.local.room.BatchDao
import com.example.enursery.core.data.source.local.room.PlotDao
import com.example.enursery.core.data.source.local.room.RoleDao
import com.example.enursery.core.data.source.local.room.UserDao
import com.example.enursery.core.data.source.local.room.VgmDao
import com.example.enursery.core.data.source.local.room.VgmHistoryDao
import com.example.enursery.core.data.source.local.room.WilayahKerjaDao
import com.example.enursery.core.domain.model.SortOption

class LocalDataSource private constructor(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val wilayahKerjaDao: WilayahKerjaDao,
    private val plotDao: PlotDao,
    private val vgmDao: VgmDao,
    private val batchDao: BatchDao,
    private val vgmHistoryDao: VgmHistoryDao
) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(userDao: UserDao,
                        roleDao: RoleDao,
                        wilayahKerjaDao: WilayahKerjaDao,
                        plotDao: PlotDao,
                        vgmDao: VgmDao,
                        batchDao: BatchDao,
                        vgmHistoryDao: VgmHistoryDao
                        ): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(
                    userDao,
                    roleDao,
                    wilayahKerjaDao,
                    plotDao,
                    vgmDao,
                    batchDao,
                    vgmHistoryDao
                )
            }
    }

//    USER
    suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
    fun getUserById(userId: String): LiveData<UserEntity> {
        return userDao.getUserById(userId)
    }
    fun getAllUsers(): LiveData<List<UserEntity>> = userDao.getAllUsers()
    suspend fun insertUserList(users: List<UserEntity>) {
        users.forEach { userDao.insertUser(it) }
    }
    suspend fun getLastUser(): UserEntity? = userDao.getLastUser()
    fun getAllRoles(): LiveData<List<RoleEntity>> = roleDao.getAllRoles()
    fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>> = wilayahKerjaDao.getAllWilayahKerja()

//    PLOT
    fun getAllPlots(): LiveData<List<PlotEntity>> = plotDao.getAllPlots()
    fun getPlotsWithVgmCount(): LiveData<List<PlotWithVgmCount>> {
        return plotDao.getPlotsWithVgmCount()
    }
    suspend fun insertPlots(plots: List<PlotEntity>) = plotDao.insertPlots(plots)
    suspend fun insertSinglePlot(plot: PlotEntity) {
        Log.d("LocalDataSource", "Insert plot: ${plot.idPlot}, ${plot.namaPlot}")
        plotDao.insertSinglePlot(plot)
    }
    suspend fun updatePlot(plot: PlotEntity) = plotDao.updatePlot(plot)
    suspend fun deletePlotById(idPlot: String) = plotDao.deletePlotById(idPlot)

//    VGM
    fun getAllVgm(): LiveData<List<VgmEntity>> = vgmDao.getAllVgm()

    fun insertVgmList(list: List<VgmEntity>) = vgmDao.insertVgm(list)

    fun insertSingleVgm(vgm: VgmEntity) = vgmDao.insertSingleVgm(vgm)

    fun getAllVgmWithUser(): LiveData<List<VgmWithUser>> = vgmDao.getAllVgmWithUser()

    fun getSortedVgm(sortOption: SortOption): LiveData<List<VgmWithUser>> {
        return getAllVgmWithUser().map { list ->
            when (sortOption) {
                SortOption.TERBARU -> list.sortedByDescending { it.vgm.latestTimestamp }
                SortOption.TERLAMA -> list.sortedBy { it.vgm.latestTimestamp }
                SortOption.ID_AZ -> list.sortedBy { it.vgm.idBibit }
                SortOption.ID_ZA -> list.sortedByDescending { it.vgm.idBibit }
            }
        }
    }

    //    VGM HISTORY
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistoryEntity>> =
        vgmHistoryDao.getHistoryByBibit(idBibit)

    suspend fun insertVgmHistory(history: VgmHistoryEntity) =
        vgmHistoryDao.insertVgmHistory(history)

    // ⚠️ Ini hanya untuk seeder/fetch awal → jangan dipakai di VgmRepository
    fun getLatestVgmFromHistory(): LiveData<List<VgmHistoryEntity>> =
        vgmHistoryDao.getLatestVgmFromHistory()

    // ----------------------------------------
    // BATCH
    // ----------------------------------------

    fun getAllBatch(): LiveData<List<BatchEntity>> = batchDao.getAllBatch()
}

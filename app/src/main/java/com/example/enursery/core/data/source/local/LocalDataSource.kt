package com.example.enursery.core.data.source.local

import android.util.Log
import androidx.lifecycle.LiveData
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
import com.example.enursery.core.domain.model.VgmWithUserModel

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
    fun insertVgm(vgm: List<VgmEntity>) = vgmDao.insertVgm(vgm)
    fun insertVgmItem(vgm: VgmEntity) {
        vgmDao.insertSingleVgm(vgm)
    }
    fun getAllVgmWithUser(): LiveData<List<VgmWithUserModel>> {
        return vgmDao.getAllVgmWithUser()
    }

    fun getAllVgmWithUserRel(): LiveData<List<VgmWithUser>> {
        return vgmDao.getAllVgmWithUserRel()
    }

//    VGM HISTORY
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistoryEntity>> {
        return vgmHistoryDao.getHistoryByBibit(idBibit)
    }

    suspend fun insertVgmHistory(history: VgmHistoryEntity) {
        vgmHistoryDao.insertVgmHistory(history)
    }

    fun getLatestVgmFromHistory(): LiveData<List<VgmHistoryEntity>> {
        return vgmHistoryDao.getLatestVgmFromHistory()
    }

    //    BATCH
    fun getAllBatch(): LiveData<List<BatchEntity>> {
        return batchDao.getAllBatch()
    }
}

package com.example.enursery.core.data.source.local

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.UserEntity
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.data.source.local.room.PlotDao
import com.example.enursery.core.data.source.local.room.RoleDao
import com.example.enursery.core.data.source.local.room.UserDao
import com.example.enursery.core.data.source.local.room.VgmDao
import com.example.enursery.core.data.source.local.room.WilayahKerjaDao

class LocalDataSource private constructor(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val wilayahKerjaDao: WilayahKerjaDao,
    private val plotDao: PlotDao,
    private val vgmDao: VgmDao
) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(userDao: UserDao,
                        roleDao: RoleDao,
                        wilayahKerjaDao: WilayahKerjaDao,
                        plotDao: PlotDao,
                        vgmDao: VgmDao
                        ): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?:LocalDataSource(userDao, roleDao, wilayahKerjaDao, plotDao, vgmDao)
            }
    }

    suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    fun getAllUsers(): LiveData<List<UserEntity>> = userDao.getAllUsers()

    suspend fun insertUserList(users: List<UserEntity>) {
        users.forEach { userDao.insertUser(it) }
    }

    suspend fun getLastUser(): UserEntity? = userDao.getLastUser()

    fun getAllRoles(): LiveData<List<RoleEntity>> = roleDao.getAllRoles()
    fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>> = wilayahKerjaDao.getAllWilayahKerja()

    fun getAllPlots(): LiveData<List<PlotEntity>> = plotDao.getAllPlots()
    fun insertPlots(plots: List<PlotEntity>) = plotDao.insertPlots(plots)

    fun getAllVgm(): LiveData<List<VgmEntity>> = vgmDao.getAllVgm()
    fun insertVgm(vgm: List<VgmEntity>) = vgmDao.insertVgm(vgm)

    fun getPlotsWithVgmCount(): LiveData<List<PlotWithVgmCount>> {
        return plotDao.getPlotsWithVgmCount()
    }
}

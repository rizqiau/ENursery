package com.example.enursery.core.data.source

import com.example.enursery.core.data.source.local.room.RoleDao
import com.example.enursery.core.data.source.local.room.WilayahKerjaDao
import com.example.enursery.core.data.source.remote.RemoteDataSource

class MasterDataSeeder(
    private val remoteDataSource: RemoteDataSource,
    private val roleDao: RoleDao,
    private val wilayahDao: WilayahKerjaDao
) {
    suspend fun seedIfNeeded() {
        if (roleDao.getCount() == 0) {
            val roles = remoteDataSource.getRoleData()
            roleDao.insertAll(roles)
        }

        if (wilayahDao.getCount() == 0) {
            val wilayah = remoteDataSource.getWilayahData()
            wilayahDao.insertAll(wilayah)
        }
    }
}

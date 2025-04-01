package com.example.enursery.core.data.source

import com.example.enursery.core.data.source.local.room.BatchDao
import com.example.enursery.core.data.source.local.room.PlotDao
import com.example.enursery.core.data.source.local.room.RoleDao
import com.example.enursery.core.data.source.local.room.WilayahKerjaDao
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.utils.mapper.BatchMapper
import com.example.enursery.core.utils.mapper.PlotMapper

class MasterDataSeeder(
    private val remoteDataSource: RemoteDataSource,
    private val roleDao: RoleDao,
    private val wilayahDao: WilayahKerjaDao,
    private val batchDao: BatchDao,
    private val plotDao: PlotDao
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

        if (batchDao.getAllBatch().value.isNullOrEmpty()) {
            val batches = remoteDataSource.getBatchData()
            val entities = BatchMapper.mapResponsesToEntities(batches)
            batchDao.insertBatch(entities)
        }

        if (plotDao.getAllPlots().value.isNullOrEmpty()) {
            val plotResponse = remoteDataSource.getPlotDataRaw()
            val plotEntities = PlotMapper.mapPlotResponseToEntities(plotResponse)
            plotDao.insertPlots(plotEntities)
        }
    }
}

package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.repository.IBatchRepository
import com.example.enursery.core.utils.mapper.BatchMapper

class BatchRepository(
    private val localDataSource: LocalDataSource
) : IBatchRepository {

    override fun getAllBatch(): LiveData<List<Batch>> {
        return localDataSource.getAllBatch().map {
            BatchMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun insertBatch(batch: Batch) {
        val entity = BatchMapper.mapDomainToEntities(batch)
        localDataSource.insertSingleBatch(entity)
    }

    override suspend fun updateBatch(batch: Batch) {
        val entity = BatchMapper.mapDomainToEntities(batch)
        localDataSource.updateBatch(entity)
    }

    companion object {
        @Volatile
        private var instance: BatchRepository? = null

        fun getInstance(localDataSource: LocalDataSource): BatchRepository =
            instance ?: synchronized(this) {
                instance ?: BatchRepository(localDataSource).also { instance = it }
            }
    }
}

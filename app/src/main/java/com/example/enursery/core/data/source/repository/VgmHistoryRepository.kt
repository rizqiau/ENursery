package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.repository.IVgmHistoryRepository
import com.example.enursery.core.utils.mapper.VgmHistoryMapper

class VgmHistoryRepository(
    private val localDataSource: LocalDataSource
) : IVgmHistoryRepository {

    override fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>> {
        return localDataSource.getHistoryByBibit(idBibit).map {
            it.map { entity -> VgmHistoryMapper.mapEntityToDomain(entity) }
        }
    }

    override suspend fun insertVgmHistory(history: VgmHistory): Result<Unit> {
        return try {
            val entity = VgmHistoryMapper.mapDomainToEntity(history)
            localDataSource.insertVgmHistory(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: VgmHistoryRepository? = null

        fun getInstance(localDataSource: LocalDataSource): VgmHistoryRepository =
            instance ?: synchronized(this) {
                instance ?: VgmHistoryRepository(localDataSource).also { instance = it }
            }
    }
}

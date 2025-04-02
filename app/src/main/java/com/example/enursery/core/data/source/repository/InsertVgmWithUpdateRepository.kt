package com.example.enursery.core.data.source.repository

import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.repository.IInsertVgmWithUpdateRepository
import com.example.enursery.core.utils.mapper.VgmHistoryMapper
import com.example.enursery.core.utils.mapper.VgmMapper

class InsertVgmWithUpdateRepository(
    private val localDataSource: LocalDataSource
) : IInsertVgmWithUpdateRepository {

    override suspend fun insertVgmHistoryAndUpdateSnapshot(history: VgmHistory): Result<Unit> {
        return try {
            // 1. Convert domain → entity
            val historyEntity = VgmHistoryMapper.mapDomainToEntity(history)

            // 2. Insert ke tabel vgm_history
            localDataSource.insertVgmHistory(historyEntity)

            // 3. Buat snapshot dari history → vgm entity
            val vgmEntity = VgmMapper.mapHistoryToVgmEntity(historyEntity)

            // 4. Update ke tabel vgm (snapshot)
            localDataSource.insertSingleVgm(vgmEntity)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: InsertVgmWithUpdateRepository? = null

        fun getInstance(localDataSource: LocalDataSource): InsertVgmWithUpdateRepository =
            instance ?: synchronized(this) {
                instance ?: InsertVgmWithUpdateRepository(localDataSource).also { instance = it }
            }
    }
}

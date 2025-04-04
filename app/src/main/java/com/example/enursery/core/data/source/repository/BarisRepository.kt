package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.domain.model.Baris
import com.example.enursery.core.domain.repository.IBarisRepository
import com.example.enursery.core.utils.mapper.BarisMapper

class BarisRepository(
    private val localDataSource: LocalDataSource
) : IBarisRepository {

    override fun getBarisByPlot(idPlot: String): LiveData<List<Baris>> {
        return localDataSource.getBarisByPlot(idPlot).map {
            BarisMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun insertBaris(baris: List<Baris>) {
        val entities = BarisMapper.mapDomainToEntities(baris)
        localDataSource.insertBarisList(entities)
    }

    override suspend fun updateBaris(baris: Baris) {
        val entity = BarisMapper.mapDomainToEntity(baris)
        localDataSource.updateBaris(entity)
    }

    override suspend fun deleteBarisByPlot(idPlot: String) {
        localDataSource.deleteBarisByPlot(idPlot)
    }
}

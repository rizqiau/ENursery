package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.NetworkBoundResource
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.remote.network.ApiResponse
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.repository.IVgmRepository
import com.example.enursery.core.utils.AppExecutors
import com.example.enursery.core.utils.DataMapper

class VgmRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IVgmRepository {

    override fun getAllVgm(): LiveData<Resource<List<Vgm>>> {
        return object : NetworkBoundResource<List<Vgm>, List<VgmResponse>>(appExecutors) {

            override fun loadFromDB(): LiveData<List<Vgm>> {
                return localDataSource.getAllVgm().map {
                    DataMapper.mapVgmEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Vgm>?): Boolean {
                return data.isNullOrEmpty() // fetch jika kosong
            }

            override fun createCall(): LiveData<ApiResponse<List<VgmResponse>>> {
                return remoteDataSource.getVgmData()
            }

            override suspend fun saveCallResult(data: List<VgmResponse>) {
                val vgmEntities = DataMapper.mapVgmResponseToEntities(data)
                localDataSource.insertVgm(vgmEntities)
            }
        }.asLiveData()
    }

    companion object {
        @Volatile
        private var instance: VgmRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): VgmRepository =
            instance ?: synchronized(this) {
                instance ?: VgmRepository(remoteDataSource, localDataSource, appExecutors).also {
                    instance = it
                }
            }
    }
}
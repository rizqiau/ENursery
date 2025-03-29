package com.example.enursery.core.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.NetworkBoundResource
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.remote.network.ApiResponse
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.repository.IPlotRepository
import com.example.enursery.core.utils.AppExecutors
import com.example.enursery.core.utils.DataMapper

class PlotRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IPlotRepository {

    override fun getAllPlots(): LiveData<Resource<List<Plot>>> {
        return object : NetworkBoundResource<List<Plot>, List<PlotResponse>>(appExecutors) {

            override fun loadFromDB(): LiveData<List<Plot>> {
               return localDataSource.getAllPlots().map {
                   DataMapper.mapPlotEntitiesToDomain(it)
               }
            }

            override fun shouldFetch(data: List<Plot>?): Boolean {
                return data.isNullOrEmpty() // fetch jika kosong
            }

            override fun createCall(): LiveData<ApiResponse<List<PlotResponse>>> {
                return remoteDataSource.getPlotData()
            }

            override suspend fun saveCallResult(data: List<PlotResponse>) {
                val plotEntities = DataMapper.mapPlotResponseToEntities(data)
                localDataSource.insertPlots(plotEntities)
            }
        }.asLiveData()
    }

    override fun getPlotsWithVgmCount(): LiveData<Resource<List<PlotWithVgmCountModel>>> {
        return localDataSource.getPlotsWithVgmCount().map { list ->
            Log.d("PlotRepository", "VGMs from Room: ${list.map { it.jumlahVgm }}")
            Resource.Success(DataMapper.mapPlotWithVgmEntityToDomain(list))
        }
    }

    companion object {
        @Volatile
        private var instance: PlotRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): PlotRepository =
            instance ?: synchronized(this) {
                instance ?: PlotRepository(remoteDataSource, localDataSource, appExecutors).also {
                    instance = it
                }
            }
    }
}

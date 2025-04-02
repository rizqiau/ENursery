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
import com.example.enursery.core.utils.mapper.PlotMapper

class PlotRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IPlotRepository {

    override fun getAllPlots(): LiveData<Resource<List<Plot>>> {
        return object : NetworkBoundResource<List<Plot>, List<PlotResponse>>(appExecutors) {

            override fun loadFromDB(): LiveData<List<Plot>> {
               return localDataSource.getAllPlots().map {
                   PlotMapper.mapPlotEntitiesToDomain(it)
               }
            }

            override fun shouldFetch(data: List<Plot>?): Boolean {
                return data.isNullOrEmpty() // fetch jika kosong
            }

            override fun createCall(): LiveData<ApiResponse<List<PlotResponse>>> {
                return remoteDataSource.getPlotData()
            }

            override suspend fun saveCallResult(data: List<PlotResponse>) {
                val plotEntities = PlotMapper.mapPlotResponseToEntities(data)
                localDataSource.insertPlots(plotEntities)
            }
        }.asLiveData()
    }

    override fun getPlotsWithVgmCount(): LiveData<Resource<List<PlotWithVgmCountModel>>> {
        return localDataSource.getPlotsWithVgmCount().map { list ->
            try {
                val mappedData = PlotMapper.mapPlotWithVgmEntityToDomain(list)
                Resource.Success(mappedData)
            } catch (e: Exception) {
                Log.e("PlotRepository", "Gagal mapping PlotWithVgm: ${e.message}")
                Resource.Error("Gagal memuat data plot", null)
            }
        }
    }

    override suspend fun insertSinglePlot(plot: Plot) {
        val entity = PlotMapper.mapPlotDomainToEntity(plot)
        try {
            localDataSource.insertSinglePlot(entity) // make sure ini suspend
        } catch (e: Exception) {
            Log.e("PlotRepository", "Insert failed: ${e.message}")
            throw e
        }
    }

    override suspend fun updatePlot(plot: Plot) {
        val entity = PlotMapper.mapPlotDomainToEntity(plot)
        localDataSource.updatePlot(entity)
    }

    override suspend fun deletePlotById(idPlot: String) {
        localDataSource.deletePlotById(idPlot)
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

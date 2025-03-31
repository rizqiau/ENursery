package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel

interface IPlotRepository {
    fun getAllPlots(): LiveData<Resource<List<Plot>>>
    fun getPlotsWithVgmCount(): LiveData<Resource<List<PlotWithVgmCountModel>>>
    suspend fun insertSinglePlot(plot: Plot)
    suspend fun updatePlot(plot: Plot)
    suspend fun deletePlotById(idPlot: String)
}

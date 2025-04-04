package com.example.enursery.core.domain.usecase.plot

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.repository.IPlotRepository

class PlotInteractor(private val repository: IPlotRepository) : PlotUseCase {
    override fun getAllPlots(): LiveData<Resource<List<Plot>>> {
        return repository.getAllPlots()
    }

    override fun getPlotsWithVgmCount(): LiveData<Resource<List<PlotWithVgmCountModel>>> {
        return repository.getPlotsWithVgmCount()
    }

    override suspend fun insertSinglePlot(plot: Plot) {
        repository.insertSinglePlot(plot)
    }

    override suspend fun updatePlot(plot: Plot) {
        repository.updatePlot(plot)
    }

    override suspend fun deletePlotById(idPlot: String) {
        repository.deletePlotById(idPlot)
    }
}

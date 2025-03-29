package com.example.enursery.core.domain.usecase

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
}

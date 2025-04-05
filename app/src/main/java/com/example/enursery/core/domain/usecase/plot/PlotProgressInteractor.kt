package com.example.enursery.core.domain.usecase.plot

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.PlotProgressModel
import com.example.enursery.core.domain.repository.IPlotProgressRepository


class PlotProgressInteractor(
    private val repository: IPlotProgressRepository
) : PlotProgressUseCase {

    override fun getPlotProgressByUser(userId: String): LiveData<List<PlotProgressModel>> {
        return repository.getPlotProgressByUser(userId)
    }
}


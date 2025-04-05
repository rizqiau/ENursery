package com.example.enursery.core.domain.usecase.plot

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.PlotProgressModel

interface PlotProgressUseCase {
    fun getPlotProgressByUser(userId: String): LiveData<List<PlotProgressModel>>
}

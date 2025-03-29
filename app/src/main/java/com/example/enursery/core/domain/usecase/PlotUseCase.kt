package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel

interface PlotUseCase {
    fun getAllPlots(): LiveData<Resource<List<Plot>>>
    fun getPlotsWithVgmCount(): LiveData<Resource<List<PlotWithVgmCountModel>>>
}

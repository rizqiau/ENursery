package com.example.enursery.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.usecase.PlotUseCase

class HomeViewModel(private val plotUseCase: PlotUseCase): ViewModel() {
    val plots: LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()
    val plotWithVgm: LiveData<Resource<List<PlotWithVgmCountModel>>> = plotUseCase.getPlotsWithVgmCount()
}
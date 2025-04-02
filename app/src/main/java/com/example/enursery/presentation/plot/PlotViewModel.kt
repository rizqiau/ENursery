package com.example.enursery.presentation.plot

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.usecase.PlotUseCase

class PlotViewModel(
    private val plotUseCase: PlotUseCase
) : ViewModel() {

    val plotList: LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()
}

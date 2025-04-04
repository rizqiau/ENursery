package com.example.enursery.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Baris
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithBarisModel
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.usecase.baris.BarisUseCase
import com.example.enursery.core.domain.usecase.plot.PlotUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val plotUseCase: PlotUseCase,
    private val barisUseCase: BarisUseCase,
    private val vgmUseCase: VgmUseCase
) : ViewModel() {
    private val _updateResult = MutableLiveData<Result<Unit>>()
    val updateResult: LiveData<Result<Unit>> = _updateResult

    private val _insertResult = MutableLiveData<Result<Unit>>()
    val insertResult: LiveData<Result<Unit>> = _insertResult

    fun getAllPlots(): LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()
    fun getPlotWithVgm(): LiveData<Resource<List<PlotWithVgmCountModel>>> = plotUseCase.getPlotsWithVgmCount()

    fun insertSinglePlot(plot: Plot, barisList: List<Baris>, vgmList: List<Vgm>) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    plotUseCase.insertSinglePlot(plot)
                    barisUseCase.insertBaris(barisList)
                    vgmUseCase.insertVgmList(vgmList)
                }
                _insertResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _insertResult.value = Result.failure(e)
                Log.d("gagal euy", "$e")
            }
        }
    }

    fun updatePlot(plot: Plot, barisList: List<Baris>) {
        viewModelScope.launch {
            try {
                plotUseCase.updatePlot(plot)
                barisUseCase.deleteBarisByPlot(plot.idPlot)
                barisUseCase.insertBaris(barisList)
                _updateResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _updateResult.value = Result.failure(e)
            }
        }
    }

    fun deletePlot(idPlot: String) {
        viewModelScope.launch {
            try {
                plotUseCase.deletePlotById(idPlot)
            } catch (e: Exception) {
                Log.e("PlotVM", "Gagal delete: $e")
            }
        }
    }

    fun getPlotWithBaris(idPlot: String): LiveData<PlotWithBarisModel> {
        return plotUseCase.getPlotWithBaris(idPlot)
    }

    suspend fun insertVgmList(vgmList: List<Vgm>) = vgmUseCase.insertVgmList(vgmList)
}
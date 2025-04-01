package com.example.enursery.presentation.vgm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.usecase.BatchUseCase
import com.example.enursery.core.domain.usecase.PlotUseCase
import com.example.enursery.core.domain.usecase.SessionUseCase
import com.example.enursery.core.domain.usecase.VgmHistoryUseCase
import com.example.enursery.core.domain.usecase.VgmUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class VgmViewModel(
    private val vgmUseCase: VgmUseCase,
    private val batchUseCase: BatchUseCase,
    private val plotUseCase: PlotUseCase,
    private val vgmHistoryUseCase: VgmHistoryUseCase,
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val vgm: LiveData<Resource<List<Vgm>>> = vgmUseCase.getAllVgm()
    val batchList: LiveData<List<Batch>> = batchUseCase.getAllBatch()
    val plotList: LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()

    var currentBatch: List<Batch> = emptyList()
    var currentPlot: List<Plot> = emptyList()

    fun getCurrentUserId(): String? {
        return sessionUseCase.getUserId()
    }

    fun insertVgmHistory(
        idBibit: String,
        idPlot: String,
        idUser: String,
        idBatch: String,
        tinggi: Double,
        diameter: Double,
        jumlahDaun: Int,
        fotoPath: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val newHistory = VgmHistory(
                    id = UUID.randomUUID().toString(),
                    idBibit = idBibit,
                    idPlot = idPlot,
                    idUser = idUser,
                    idBatch = idBatch,
                    tinggi = tinggi,
                    diameter = diameter,
                    jumlahDaun = jumlahDaun,
                    tanggalInput = Date(),
                    foto = fotoPath
                )

                val result = withContext(Dispatchers.IO) {
                    vgmHistoryUseCase.insertVgmHistory(newHistory)
                }

                if (result.isSuccess) {
                    val latestVgm = Vgm(
                        idBibit = idBibit,
                        idPlot = idPlot,
                        idUser = idUser,
                        status = "Aktif",
                        latestTinggiTanaman = tinggi,
                        latestDiameterBatang = diameter,
                        latestJumlahDaun = jumlahDaun,
                        latestTanggalInput = newHistory.tanggalInput,
                        latestFoto = fotoPath
                    )

                    withContext(Dispatchers.IO) {
                        vgmUseCase.insertVgm(latestVgm)
                    }

                    Log.d("VgmViewModel", "Insert VGM sukses untuk $idBibit")
                }

                onResult(result)

            } catch (e: Exception) {
                Log.e("VgmViewModel", "Insert gagal: ${e.message}")
                onResult(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}

package com.example.enursery.presentation.vgm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.identity.util.UUID
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.model.DateFilterType
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.SortOption
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.model.VgmWithUserModel
import com.example.enursery.core.domain.usecase.batch.BatchUseCase
import com.example.enursery.core.domain.usecase.plot.PlotUseCase
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.vgm.InsertVgmWithUpdateUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class VgmViewModel(
    private val vgmUseCase: VgmUseCase,
    private val batchUseCase: BatchUseCase,
    private val plotUseCase: PlotUseCase,
    private val vgmHistoryUseCase: VgmHistoryUseCase,
    private val sessionUseCase: SessionUseCase,
    private val insertVgmWithUpdateUseCase: InsertVgmWithUpdateUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _sortOption = MutableLiveData(SortOption.TERBARU)
    val sortOption: LiveData<SortOption> get() = _sortOption

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _selectedBatchId = MutableLiveData<String?>()
    val selectedBatchId: LiveData<String?> get() = _selectedBatchId

    private val _selectedPlotId = MutableLiveData<String?>()
    val selectedPlotId: LiveData<String?> get() = _selectedPlotId

    private val _selectedDateFilter = MutableLiveData<Pair<DateFilterType, LocalDate?>?>()
    val selectedDateFilter: MutableLiveData<Pair<DateFilterType, LocalDate?>?> = _selectedDateFilter

    private val _selectedDateRange = MutableLiveData<Pair<LocalDate, LocalDate>?>()
    val selectedDateRange: LiveData<Pair<LocalDate, LocalDate>?> = _selectedDateRange

    val vgmWithUserModel: LiveData<List<VgmWithUserModel>> = vgmUseCase.getAllVgmWithUser()
    val batchList: LiveData<List<Batch>> = batchUseCase.getAllBatch()
    val plotList: LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()
    var currentBatch: List<Batch> = emptyList()
    var currentPlot: List<Plot> = emptyList()

    fun getCurrentUserId(): String? = sessionUseCase.getUserId()

    fun getCurrentUserRole(): String? = sessionUseCase.getUserRole()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedPlot(plotId: String?) {
        _selectedPlotId.value = plotId
    }

    fun setSelectedBatch(batchId: String?) {
        _selectedBatchId.value = batchId
    }

    fun setDateFilter(type: DateFilterType, date: LocalDate?) {
        _selectedDateFilter.value = Pair(type, date)
    }

    fun setDateRangeFilter(start: LocalDate, end: LocalDate) {
        _selectedDateFilter.value = Pair(DateFilterType.PILIH_RENTANG_TANGGAL, null)
        _selectedDateRange.value = Pair(start, end)
    }

    fun resetDateFilter() {
        _selectedDateFilter.value = null
        _selectedDateRange.value = null
    }

    fun insertVgmHistory(
        idBibit: String,
        idPlot: String,
        idUser: String,
        idBatch: String,
        status: String,
        tinggi: Double,
        diameter: Double,
        jumlahDaun: Int,
        fotoPath: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val newHistory = VgmHistory(
                id = UUID.randomUUID().toString(),
                idBibit = idBibit,
                idPlot = idPlot,
                idUser = idUser,
                idBatch = idBatch,
                status = status,
                tinggi = tinggi,
                diameter = diameter,
                jumlahDaun = jumlahDaun,
                tanggalInput = LocalDate.now(),
                foto = fotoPath
            )

            val result = withContext(Dispatchers.IO) {
                insertVgmWithUpdateUseCase.execute(newHistory)
            }

            if (result.isSuccess) {
                Log.d("VgmViewModel", "Insert VGM + History sukses untuk $idBibit")
            } else {
                Log.e("VgmViewModel", "Insert gagal: ${result.exceptionOrNull()?.message}")
            }

            onResult(result)
            _isLoading.value = false
        }
    }

    val filteredSortedVgm: LiveData<List<VgmWithUserModel>> =
        MediatorLiveData<List<VgmWithUserModel>>().apply {
            var currentList: List<VgmWithUserModel> = emptyList()
            var currentSort: SortOption = SortOption.TERBARU
            var currentQuery: String = ""
            var currentBatchId: String? = null
            var currentPlotId: String? = null
            var currentDateFilter: Pair<DateFilterType, LocalDate?>? = null
            var currentDateRange: Pair<LocalDate, LocalDate>? = null

            fun update() {
                val localDateFilter = currentDateFilter
                val localDateRange = currentDateRange

                val batchFiltered = if (!currentBatchId.isNullOrEmpty()) {
                    currentList.filter { it.idBatch == currentBatchId }
                } else currentList

                val plotFiltered = if (!currentPlotId.isNullOrEmpty()) {
                    batchFiltered.filter { it.idPlot == currentPlotId }
                } else batchFiltered

                val dateFiltered = localDateFilter?.let { filter ->
                    when (filter.first) {
                        DateFilterType.HARI_INI -> {
                            plotFiltered.filter { it.latestTanggalInput == LocalDate.now() }
                        }
                        DateFilterType.TUJUH_HARI_TERAKHIR -> {
                            val limit = LocalDate.now().minusDays(6)
                            plotFiltered.filter { it.latestTanggalInput >= limit }
                        }
                        DateFilterType.PILIH_RENTANG_TANGGAL -> {
                            localDateRange?.let { (start, end) ->
                                plotFiltered.filter { it.latestTanggalInput in start..end }
                            } ?: plotFiltered
                        }
                        DateFilterType.PILIH_BULAN -> {
                            plotFiltered.filter {
                                it.latestTanggalInput.month == filter.second?.month &&
                                        it.latestTanggalInput.year == filter.second?.year
                            }
                        }
                    }
                } ?: plotFiltered

                val searched = dateFiltered.filter {
                    it.idBibit.contains(currentQuery, ignoreCase = true)
                }

                val sorted = when (currentSort) {
                    SortOption.TERBARU -> searched.sortedByDescending { it.latestTimestamp }
                    SortOption.TERLAMA -> searched.sortedBy { it.latestTimestamp }
                    SortOption.ID_AZ -> searched.sortedBy { it.idBibit }
                    SortOption.ID_ZA -> searched.sortedByDescending { it.idBibit }
                }

                value = sorted
            }

            addSource(vgmUseCase.getAllVgmWithUser()) {
                currentList = it ?: emptyList()
                update()
            }

            addSource(_sortOption) {
                currentSort = it
                update()
            }

            addSource(_searchQuery) {
                currentQuery = it.orEmpty()
                update()
            }

            addSource(_selectedBatchId) {
                currentBatchId = it
                update()
            }

            addSource(_selectedPlotId) {
                currentPlotId = it
                update()
            }

            addSource(_selectedDateFilter) {
                currentDateFilter = it
                update()
            }

            addSource(_selectedDateRange) {
                currentDateRange = it
                update()
            }
        }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun insertBatch(batch: Batch, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = try {
                withContext(Dispatchers.IO) {
                    batchUseCase.insertBatch(batch)
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
            _isLoading.value = false
            onResult(result)
        }
    }
}


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

    private val _selectedStatus = MutableLiveData<String?>()
    val selectedStatus: LiveData<String?> = _selectedStatus

    private val _selectedDateFilter = MutableLiveData<Pair<DateFilterType, LocalDate?>?>()
    val selectedDateFilter: MutableLiveData<Pair<DateFilterType, LocalDate?>?> = _selectedDateFilter

    private val _selectedDateRange = MutableLiveData<Pair<LocalDate, LocalDate>?>()
    val selectedDateRange: LiveData<Pair<LocalDate, LocalDate>?> = _selectedDateRange

    val batchList: LiveData<List<Batch>> = batchUseCase.getAllBatch()
    val plotList: LiveData<Resource<List<Plot>>> = plotUseCase.getAllPlots()
    var currentBatch: List<Batch> = emptyList()
    var currentPlot: List<Plot> = emptyList()

    fun getCurrentUserId(): String? = sessionUseCase.getUserId()

    fun getCurrentUserName(): String? = sessionUseCase.getUserName()

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

    fun setSelectedStatus(status: String?) {
        Log.d("VgmViewModel", "Status set: $status") // optional
        _selectedStatus.value = status
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
        idBaris: String,
        idUser: String,
        namaUser: String,
        idBatch: String,
        status: String,
        tinggi: Double,
        diameter: Double,
        jumlahDaun: Int,
        lebar: Double,
        fotoPath: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val newHistory = VgmHistory(
                id = UUID.randomUUID().toString(),
                idBibit = idBibit,
                idPlot = idPlot,
                idBaris = idBaris,
                idUser = idUser,
                namaUser = namaUser,
                idBatch = idBatch,
                status = status,
                tinggi = tinggi,
                diameter = diameter,
                jumlahDaun = jumlahDaun,
                lebarPetiole = lebar,
                tanggalInput = LocalDate.now(),              // ✅ fix: LocalDate
                createdAt = System.currentTimeMillis(),      // ✅ fix: epochMillis
                foto = fotoPath
            )

            val result = withContext(Dispatchers.IO) {
                Log.d("VgmDebug", "Insert VGM → idPlot=${idPlot}, idBaris=${idBaris}, idUser=${idUser}")
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
            var currentStatus: String? = null

            fun applyFilters(): List<VgmWithUserModel> {
                return currentList
                    .filterByBatch(currentBatchId)
                    .filterByPlot(currentPlotId)
                    .filterByDate(currentDateFilter, currentDateRange)
                    .filterByStatus(currentStatus)
                    .filterBySearchQuery(currentQuery)
            }

            fun update() {
                val filtered = applyFilters()
                val sorted = when (currentSort) {
                    SortOption.TERBARU -> filtered.sortedByDescending { it.latestTanggalInput }
                    SortOption.TERLAMA -> filtered.sortedBy { it.latestTanggalInput }
                    SortOption.ID_AZ -> filtered.sortedBy { it.idBibit }
                    SortOption.ID_ZA -> filtered.sortedByDescending { it.idBibit }
                }
                value = sorted
            }

            // Sources
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
            addSource(_selectedStatus) {
                currentStatus = it
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

    suspend fun isBibitExist(idBibit: String): Boolean {
        return vgmUseCase.isBibitExist(idBibit)
    }
}
private fun List<VgmWithUserModel>.filterByBatch(batchId: String?) =
    if (!batchId.isNullOrEmpty()) filter { it.idBatch == batchId } else this

private fun List<VgmWithUserModel>.filterByPlot(plotId: String?) =
    if (!plotId.isNullOrEmpty()) filter { it.idPlot == plotId } else this

private fun List<VgmWithUserModel>.filterByDate(
    dateFilter: Pair<DateFilterType, LocalDate?>?,
    dateRange: Pair<LocalDate, LocalDate>?
): List<VgmWithUserModel> {
    return dateFilter?.let { filter ->
        when (filter.first) {
            DateFilterType.HARI_INI -> {
                val today = LocalDate.now()
                filter { it.latestTanggalInput == today }
            }

            DateFilterType.TUJUH_HARI_TERAKHIR -> {
                val limit = LocalDate.now().minusDays(6)
                filter { it.latestTanggalInput != null && it.latestTanggalInput >= limit }
            }

            DateFilterType.PILIH_RENTANG_TANGGAL -> {
                dateRange?.let { (start, end) ->
                    filter { it.latestTanggalInput != null && it.latestTanggalInput in start..end }
                } ?: this
            }

            DateFilterType.PILIH_BULAN -> {
                val selected = filter.second
                filter {
                    selected != null &&
                            it.latestTanggalInput?.month == selected.month &&
                            it.latestTanggalInput?.year == selected.year
                }
            }
        }
    } ?: this
}

private fun List<VgmWithUserModel>.filterByStatus(status: String?): List<VgmWithUserModel> {
    val result = if (!status.isNullOrEmpty()) {
        val filtered = filter { it.status == status }
        Log.d("VgmFilter", "Filtered by status=$status, result size=${filtered.size}")
        filtered
    } else {
        Log.d("VgmFilter", "No status filter applied, total size=${size}")
        this
    }
    return result
}


private fun List<VgmWithUserModel>.filterBySearchQuery(query: String) =
    filter {
        it.latestTanggalInput != null &&
                it.idBibit.contains(query, ignoreCase = true)
    }


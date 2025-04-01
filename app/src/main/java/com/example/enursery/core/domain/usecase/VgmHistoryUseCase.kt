package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.VgmHistory

interface VgmHistoryUseCase {
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>>
    suspend fun insertVgmHistory(history: VgmHistory): Result<Unit>
}

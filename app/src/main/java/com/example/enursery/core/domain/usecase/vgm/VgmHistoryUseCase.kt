package com.example.enursery.core.domain.usecase.vgm

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.VgmDailyStat
import com.example.enursery.core.domain.model.VgmHistory

interface VgmHistoryUseCase {
    suspend fun insertVgmHistory(history: VgmHistory): Result<Unit>
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>>
    fun getDailyInputByUser(userId: String): LiveData<List<VgmDailyStat>>
}

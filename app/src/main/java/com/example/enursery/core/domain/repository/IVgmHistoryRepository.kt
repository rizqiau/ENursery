package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.VgmDailyStat
import com.example.enursery.core.domain.model.VgmHistory

interface IVgmHistoryRepository {
    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>>
    suspend fun insertVgmHistory(history: VgmHistory): Result<Unit>
    fun getDailyInputByUser(userId: String): LiveData<List<VgmDailyStat>>

}

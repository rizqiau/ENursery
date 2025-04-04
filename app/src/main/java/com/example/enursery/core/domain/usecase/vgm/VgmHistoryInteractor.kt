package com.example.enursery.core.domain.usecase.vgm

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.repository.IVgmHistoryRepository

class VgmHistoryInteractor(
    private val repository: IVgmHistoryRepository
) : VgmHistoryUseCase {

    override fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>> {
        return repository.getHistoryByBibit(idBibit)
    }

    override suspend fun insertVgmHistory(history: VgmHistory): Result<Unit> {
        return repository.insertVgmHistory(history)
    }
}
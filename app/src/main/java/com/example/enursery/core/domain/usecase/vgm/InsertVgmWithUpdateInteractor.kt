package com.example.enursery.core.domain.usecase.vgm

import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.repository.IInsertVgmWithUpdateRepository

class InsertVgmWithUpdateInteractor(
    private val repository: IInsertVgmWithUpdateRepository
) : InsertVgmWithUpdateUseCase {

    override suspend fun execute(history: VgmHistory): Result<Unit> {
        return repository.insertVgmHistoryAndUpdateSnapshot(history)
    }
}
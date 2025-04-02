package com.example.enursery.core.domain.usecase

import com.example.enursery.core.domain.model.VgmHistory

interface InsertVgmWithUpdateUseCase {
    suspend fun execute(history: VgmHistory): Result<Unit>
}

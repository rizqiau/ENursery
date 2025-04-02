package com.example.enursery.core.domain.repository

import com.example.enursery.core.domain.model.VgmHistory

interface IInsertVgmWithUpdateRepository {
    suspend fun insertVgmHistoryAndUpdateSnapshot(history: VgmHistory): Result<Unit>
}
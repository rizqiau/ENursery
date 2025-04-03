package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Batch

interface BatchUseCase {
    fun getAllBatch(): LiveData<List<Batch>>
    suspend fun insertBatch(batch: Batch)
    suspend fun updateBatch(batch: Batch)
}

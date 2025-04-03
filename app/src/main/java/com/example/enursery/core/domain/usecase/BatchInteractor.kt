package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.repository.IBatchRepository

class BatchInteractor(private val repository: IBatchRepository) : BatchUseCase {

    override fun getAllBatch(): LiveData<List<Batch>> {
        return repository.getAllBatch()
    }

    override suspend fun insertBatch(batch: Batch) {
        repository.insertBatch(batch)
    }

    override suspend fun updateBatch(batch: Batch) {
        repository.updateBatch(batch)
    }
}

package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.usecase.BatchUseCase

class BatchInteractor(private val repository: IBatchRepository) : BatchUseCase {

    override fun getAllBatch(): LiveData<List<Batch>> {
        return repository.getAllBatch()
    }
}

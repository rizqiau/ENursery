package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Batch

interface IBatchRepository {
    fun getAllBatch(): LiveData<List<Batch>>
}

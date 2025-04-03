package com.example.enursery.presentation.batch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.domain.usecase.BatchUseCase
import kotlinx.coroutines.launch

class BatchViewModel(private val batchUseCase: BatchUseCase) : ViewModel() {

    val batchList: LiveData<List<Batch>> = batchUseCase.getAllBatch()

    fun insertBatch(batch: Batch) = viewModelScope.launch {
        batchUseCase.insertBatch(batch)
    }

    fun updateBatch(batch: Batch) = viewModelScope.launch {
        batchUseCase.updateBatch(batch)
    }
}

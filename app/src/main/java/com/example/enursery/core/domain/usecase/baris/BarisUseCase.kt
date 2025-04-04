package com.example.enursery.core.domain.usecase.baris

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Baris

interface BarisUseCase {
    fun getBarisByPlot(idPlot: String): LiveData<List<Baris>>
    suspend fun insertBaris(baris: List<Baris>)
    suspend fun updateBaris(baris: Baris)
    suspend fun deleteBarisByPlot(idPlot: String)
}


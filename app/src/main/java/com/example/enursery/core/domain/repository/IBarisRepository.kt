package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Baris

interface IBarisRepository {
    fun getBarisByPlot(idPlot: String): LiveData<List<Baris>>
    suspend fun insertBaris(baris: List<Baris>)
    suspend fun updateBaris(baris: Baris)
    suspend fun deleteBarisByPlot(idPlot: String)
}



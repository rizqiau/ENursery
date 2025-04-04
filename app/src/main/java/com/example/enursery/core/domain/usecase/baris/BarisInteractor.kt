package com.example.enursery.core.domain.usecase.baris

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.Baris
import com.example.enursery.core.domain.repository.IBarisRepository

class BarisInteractor(private val repository: IBarisRepository) : BarisUseCase {

    override fun getBarisByPlot(idPlot: String): LiveData<List<Baris>> {
        return repository.getBarisByPlot(idPlot)
    }

    override suspend fun insertBaris(baris: List<Baris>) {
        repository.insertBaris(baris)
    }

    override suspend fun updateBaris(baris: Baris) {
        repository.updateBaris(baris)
    }

    override suspend fun deleteBarisByPlot(idPlot: String) {
        repository.deleteBarisByPlot(idPlot)
    }
}


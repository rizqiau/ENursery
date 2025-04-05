package com.example.enursery.presentation.vgm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase

class VgmHistoryViewModel(
    private val vgmHistoryUseCase: VgmHistoryUseCase
) : ViewModel() {

    fun getHistoryByBibit(idBibit: String): LiveData<List<VgmHistory>> {
        return vgmHistoryUseCase.getHistoryByBibit(idBibit)
    }
}

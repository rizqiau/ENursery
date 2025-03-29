package com.example.enursery.presentation.vgm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.usecase.VgmUseCase

class VgmViewModel(private val vgmUseCase: VgmUseCase): ViewModel() {
    val vgm: LiveData<Resource<List<Vgm>>> = vgmUseCase.getAllVgm()
}
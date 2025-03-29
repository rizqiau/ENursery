package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Vgm

interface VgmUseCase {
    fun getAllVgm(): LiveData<Resource<List<Vgm>>>
}
package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel

interface VgmUseCase {
    fun getAllVgm(): LiveData<Resource<List<Vgm>>>
    suspend fun insertVgm(vgm: Vgm)
    fun getAllVgmWithUser(): LiveData<List<VgmWithUserModel>>
    fun getAllVgmWithUserRel(): LiveData<List<VgmWithUserModel>>
}
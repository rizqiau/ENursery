package com.example.enursery.core.domain.usecase.vgm

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.SortOption
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel

interface VgmUseCase {
    fun getAllVgm(): LiveData<Resource<List<Vgm>>>
    fun getAllVgmWithUser(): LiveData<List<VgmWithUserModel>>
    fun getSortedVgm(sortOption: SortOption): LiveData<List<VgmWithUserModel>>
    suspend fun insertVgmList(vgmList: List<Vgm>)
    suspend fun isBibitExist(idBibit: String): Boolean
}
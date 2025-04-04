package com.example.enursery.core.domain.usecase.vgm

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.SortOption
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel
import com.example.enursery.core.domain.repository.IVgmRepository

class VgmInteractor(private val repository: IVgmRepository): VgmUseCase {
    override fun getAllVgm(): LiveData<Resource<List<Vgm>>> {
        return repository.getAllVgm()
    }

    override fun getAllVgmWithUser(): LiveData<List<VgmWithUserModel>> {
        return repository.getAllVgmWithUser()
    }

    override fun getSortedVgm(sortOption: SortOption): LiveData<List<VgmWithUserModel>> {
        return repository.getSortedVgm(sortOption)
    }

    override suspend fun insertVgmList(vgmList: List<Vgm>) {
        repository.insertVgmList(vgmList)
    }

    override suspend fun isBibitExist(idBibit: String): Boolean {
        return repository.isBibitExist(idBibit)
    }

}
package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Vgm

interface IVgmRepository {
    fun getAllVgm(): LiveData<Resource<List<Vgm>>>
}
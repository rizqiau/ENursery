package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.domain.model.PlotProgressModel

interface IPlotProgressRepository {
    fun getPlotProgressByUser(userId: String): LiveData<List<PlotProgressModel>>
}


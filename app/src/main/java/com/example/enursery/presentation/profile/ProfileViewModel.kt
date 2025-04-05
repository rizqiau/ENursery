package com.example.enursery.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.domain.model.PlotProgressModel
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.model.VgmDailyStat
import com.example.enursery.core.domain.usecase.plot.PlotProgressUseCase
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.user.UserUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase

class ProfileViewModel(
    private val sessionUseCase: SessionUseCase,
    private val userUseCase: UserUseCase,
    private val vgmHistoryUseCase: VgmHistoryUseCase,
    private val plotProgressUseCase: PlotProgressUseCase
) : ViewModel() {

    private val userId = sessionUseCase.getUserId()

    val currentUser: LiveData<User>? = userId?.let {
        userUseCase.getUserById(it)
    }

    val dailyInputStat: LiveData<List<VgmDailyStat>>? = userId?.let {
        vgmHistoryUseCase.getDailyInputByUser(it)
    }

    val plotProgressList: LiveData<List<PlotProgressModel>>? = userId?.let {
        plotProgressUseCase.getPlotProgressByUser(it)
    }

    fun logout() {
        sessionUseCase.logout()
    }
}

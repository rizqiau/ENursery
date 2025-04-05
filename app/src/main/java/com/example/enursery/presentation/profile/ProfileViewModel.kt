package com.example.enursery.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.model.VgmDailyStat
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.user.UserUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase

class ProfileViewModel(
    private val sessionUseCase: SessionUseCase,
    userUseCase: UserUseCase,
    vgmHistoryUseCase: VgmHistoryUseCase
) : ViewModel() {

    private val userId = sessionUseCase.getUserId()

    val currentUser: LiveData<User>? = userId?.let {
        userUseCase.getUserById(it)
    }

    val dailyInputStat: LiveData<List<VgmDailyStat>>? = userId?.let {
        vgmHistoryUseCase.getDailyInputByUser(it)
    }

    fun logout() {
        sessionUseCase.logout()
    }
}

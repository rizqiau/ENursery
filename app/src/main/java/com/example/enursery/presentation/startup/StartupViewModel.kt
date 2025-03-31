package com.example.enursery.presentation.startup

import androidx.lifecycle.ViewModel
import com.example.enursery.core.domain.usecase.SessionUseCase

class StartupViewModel(private val sessionUseCase: SessionUseCase) : ViewModel() {
    fun isUserLoggedIn(): Boolean {
        return sessionUseCase.isLoggedIn()
    }
}

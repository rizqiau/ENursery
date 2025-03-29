package com.example.enursery.core.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enursery.core.di.Injection
import com.example.enursery.core.domain.usecase.PlotUseCase
import com.example.enursery.core.domain.usecase.UserUseCase
import com.example.enursery.core.domain.usecase.VgmUseCase
import com.example.enursery.presentation.auth.AuthViewModel
import com.example.enursery.presentation.home.HomeViewModel
import com.example.enursery.presentation.vgm.VgmViewModel

class ViewModelFactory private constructor(
    private val userUseCase: UserUseCase,
    private val plotUseCase: PlotUseCase,
    private val vgmUseCase: VgmUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userUseCase) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(plotUseCase) as T
            }
            modelClass.isAssignableFrom(VgmViewModel::class.java) -> {
                VgmViewModel(vgmUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val userUseCase = Injection.provideUserUseCase(context)
                val plotUseCase = Injection.providePlotUseCase(context)
                val vgmUseCase = Injection.provideVgmUseCase(context)
                instance ?: ViewModelFactory(userUseCase, plotUseCase, vgmUseCase).also { instance = it }
            }
    }
}

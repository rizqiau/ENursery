package com.example.enursery.core.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enursery.core.di.Injection
import com.example.enursery.core.domain.usecase.baris.BarisUseCase
import com.example.enursery.core.domain.usecase.batch.BatchUseCase
import com.example.enursery.core.domain.usecase.login.LoginUseCase
import com.example.enursery.core.domain.usecase.plot.PlotUseCase
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.user.UserUseCase
import com.example.enursery.core.domain.usecase.vgm.InsertVgmWithUpdateUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmUseCase
import com.example.enursery.presentation.auth.AuthViewModel
import com.example.enursery.presentation.home.HomeViewModel
import com.example.enursery.presentation.plot.PlotViewModel
import com.example.enursery.presentation.profile.ProfileViewModel
import com.example.enursery.presentation.startup.StartupViewModel
import com.example.enursery.presentation.vgm.VgmHistoryViewModel
import com.example.enursery.presentation.vgm.VgmViewModel

class ViewModelFactory private constructor(
    private val userUseCase: UserUseCase,
    private val plotUseCase: PlotUseCase,
    private val vgmUseCase: VgmUseCase,
    private val loginUseCase: LoginUseCase,
    private val sessionUseCase: SessionUseCase,
    private val batchUseCase: BatchUseCase,
    private val vgmHistoryUseCase: VgmHistoryUseCase,
    private val insertVgmWithUpdateUseCase: InsertVgmWithUpdateUseCase,
    private val barisUseCase: BarisUseCase,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userUseCase, loginUseCase, sessionUseCase) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(plotUseCase, barisUseCase, vgmUseCase) as T
            }
            modelClass.isAssignableFrom(VgmViewModel::class.java) -> {
                VgmViewModel(vgmUseCase, batchUseCase, plotUseCase,
                    vgmHistoryUseCase, sessionUseCase, insertVgmWithUpdateUseCase) as T
            }
            modelClass.isAssignableFrom(StartupViewModel::class.java) -> {
                StartupViewModel(sessionUseCase) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(sessionUseCase, userUseCase, vgmHistoryUseCase) as T
            }
            modelClass.isAssignableFrom(PlotViewModel::class.java) -> {
                PlotViewModel(plotUseCase) as T
            }
            modelClass.isAssignableFrom(VgmHistoryViewModel::class.java) -> {
                VgmHistoryViewModel(vgmHistoryUseCase) as T
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
                val loginUseCase = Injection.provideLoginUseCase(context)
                val sessionUseCase = Injection.provideSessionUseCase(context)
                val batchUseCase = Injection.provideBatchUseCase(context)
                val vgmHistoryUseCase = Injection.provideVgmHistoryUseCase(context)
                val insertVgmWithUpdateUseCase = Injection.provideInsertVgmWithUpdateUseCase(context)
                val barisUseCase = Injection.provideBarisUseCase(context)
                instance ?: ViewModelFactory(
                    userUseCase,
                    plotUseCase,
                    vgmUseCase,
                    loginUseCase,
                    sessionUseCase,
                    batchUseCase,
                    vgmHistoryUseCase,
                    insertVgmWithUpdateUseCase,
                    barisUseCase).also { instance = it }
            }
    }
}

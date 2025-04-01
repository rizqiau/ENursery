package com.example.enursery.core.di

import android.content.Context
import com.example.enursery.core.data.source.MasterDataSeeder
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.local.SharedPreferencesHelper
import com.example.enursery.core.data.source.local.room.AppDatabase
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.repository.BatchRepository
import com.example.enursery.core.data.source.repository.PlotRepository
import com.example.enursery.core.data.source.repository.SessionRepository
import com.example.enursery.core.data.source.repository.UserRepository
import com.example.enursery.core.data.source.repository.VgmHistoryRepository
import com.example.enursery.core.data.source.repository.VgmRepository
import com.example.enursery.core.domain.repository.BatchInteractor
import com.example.enursery.core.domain.repository.IBatchRepository
import com.example.enursery.core.domain.repository.IPlotRepository
import com.example.enursery.core.domain.repository.ISessionRepository
import com.example.enursery.core.domain.repository.IUserRepository
import com.example.enursery.core.domain.repository.IVgmHistoryRepository
import com.example.enursery.core.domain.repository.IVgmRepository
import com.example.enursery.core.domain.usecase.BatchUseCase
import com.example.enursery.core.domain.usecase.LoginInteractor
import com.example.enursery.core.domain.usecase.LoginUseCase
import com.example.enursery.core.domain.usecase.PlotInteractor
import com.example.enursery.core.domain.usecase.PlotUseCase
import com.example.enursery.core.domain.usecase.SessionInteractor
import com.example.enursery.core.domain.usecase.SessionUseCase
import com.example.enursery.core.domain.usecase.UserInteractor
import com.example.enursery.core.domain.usecase.UserUseCase
import com.example.enursery.core.domain.usecase.VgmHistoryInteractor
import com.example.enursery.core.domain.usecase.VgmHistoryUseCase
import com.example.enursery.core.domain.usecase.VgmInteractor
import com.example.enursery.core.domain.usecase.VgmUseCase
import com.example.enursery.core.utils.AppExecutors
import com.example.enursery.core.utils.JsonHelper

object Injection {

    // SharedPreferences Helper
    private fun provideSharedPreferencesHelper(context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(context)
    }

    // JsonHelper
    private fun provideJsonHelper(context: Context): JsonHelper {
        return JsonHelper(context)
    }

    // RemoteDataSource
    private fun provideRemoteDataSource(context: Context): RemoteDataSource {
        return RemoteDataSource.getInstance(provideJsonHelper(context))
    }

    // LocalDataSource
    private fun provideLocalDataSource(context: Context): LocalDataSource {
        val database = AppDatabase.getInstance(context)
        return LocalDataSource.getInstance(
            userDao = database.userDao(),
            roleDao = database.roleDao(),
            wilayahKerjaDao = database.wilayahDao(),
            plotDao = database.plotDao(),
            vgmDao = database.vgmDao(),
            batchDao = database.batchDao(),
            vgmHistoryDao = database.vgmHistoryDao()
        )
    }

    // AppExecutors
    private fun provideAppExecutors(): AppExecutors = AppExecutors()

    // Seeder
    fun provideSeeder(context: Context): MasterDataSeeder {
        return MasterDataSeeder(
            remoteDataSource = provideRemoteDataSource(context),
            roleDao = AppDatabase.getInstance(context).roleDao(),
            wilayahDao = AppDatabase.getInstance(context).wilayahDao(),
            batchDao = AppDatabase.getInstance(context).batchDao(),
            plotDao = AppDatabase.getInstance(context).plotDao()
        )
    }

    // Repositories
    fun provideUserRepository(context: Context): IUserRepository {
        return UserRepository.getInstance(
            provideRemoteDataSource(context),
            provideLocalDataSource(context),
            provideAppExecutors()
        )
    }

    fun providePlotRepository(context: Context): IPlotRepository {
        return PlotRepository.getInstance(
            provideRemoteDataSource(context),
            provideLocalDataSource(context),
            provideAppExecutors()
        )
    }

    fun provideVgmRepository(context: Context): IVgmRepository {
        return VgmRepository.getInstance(
            provideRemoteDataSource(context),
            provideLocalDataSource(context),
            provideAppExecutors()
        )
    }

    fun provideSessionRepository(context: Context): ISessionRepository {
        return SessionRepository(provideSharedPreferencesHelper(context))
    }

    fun provideBatchRepository(context: Context): IBatchRepository {
        return BatchRepository.getInstance(provideLocalDataSource(context))
    }

    fun provideVgmHistoryRepository(context: Context): IVgmHistoryRepository {
        return VgmHistoryRepository.getInstance(provideLocalDataSource(context))
    }

    // UseCases
    fun provideUserUseCase(context: Context): UserUseCase {
        return UserInteractor(provideUserRepository(context))
    }

    fun providePlotUseCase(context: Context): PlotUseCase {
        return PlotInteractor(providePlotRepository(context))
    }

    fun provideVgmUseCase(context: Context): VgmUseCase {
        return VgmInteractor(provideVgmRepository(context))
    }

    fun provideSessionUseCase(context: Context): SessionUseCase {
        return SessionInteractor(provideSessionRepository(context))
    }

    fun provideLoginUseCase(context: Context): LoginUseCase {
        return LoginInteractor(provideUserRepository(context))
    }

    fun provideBatchUseCase(context: Context): BatchUseCase {
        return BatchInteractor(provideBatchRepository(context))
    }

    fun provideVgmHistoryUseCase(context: Context): VgmHistoryUseCase {
        return VgmHistoryInteractor(provideVgmHistoryRepository(context))
    }
}


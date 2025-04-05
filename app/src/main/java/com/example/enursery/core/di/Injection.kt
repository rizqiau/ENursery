package com.example.enursery.core.di

import android.content.Context
import com.example.enursery.core.data.source.MasterDataSeeder
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.local.SharedPreferencesHelper
import com.example.enursery.core.data.source.local.room.AppDatabase
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.repository.BarisRepository
import com.example.enursery.core.data.source.repository.BatchRepository
import com.example.enursery.core.data.source.repository.InsertVgmWithUpdateRepository
import com.example.enursery.core.data.source.repository.PlotProgressRepository
import com.example.enursery.core.data.source.repository.PlotRepository
import com.example.enursery.core.data.source.repository.SessionRepository
import com.example.enursery.core.data.source.repository.UserRepository
import com.example.enursery.core.data.source.repository.VgmHistoryRepository
import com.example.enursery.core.data.source.repository.VgmRepository
import com.example.enursery.core.domain.repository.IBarisRepository
import com.example.enursery.core.domain.repository.IBatchRepository
import com.example.enursery.core.domain.repository.IInsertVgmWithUpdateRepository
import com.example.enursery.core.domain.repository.IPlotProgressRepository
import com.example.enursery.core.domain.repository.IPlotRepository
import com.example.enursery.core.domain.repository.ISessionRepository
import com.example.enursery.core.domain.repository.IUserRepository
import com.example.enursery.core.domain.repository.IVgmHistoryRepository
import com.example.enursery.core.domain.repository.IVgmRepository
import com.example.enursery.core.domain.usecase.baris.BarisInteractor
import com.example.enursery.core.domain.usecase.baris.BarisUseCase
import com.example.enursery.core.domain.usecase.batch.BatchInteractor
import com.example.enursery.core.domain.usecase.batch.BatchUseCase
import com.example.enursery.core.domain.usecase.login.LoginInteractor
import com.example.enursery.core.domain.usecase.login.LoginUseCase
import com.example.enursery.core.domain.usecase.plot.PlotInteractor
import com.example.enursery.core.domain.usecase.plot.PlotProgressInteractor
import com.example.enursery.core.domain.usecase.plot.PlotProgressUseCase
import com.example.enursery.core.domain.usecase.plot.PlotUseCase
import com.example.enursery.core.domain.usecase.user.SessionInteractor
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.user.UserInteractor
import com.example.enursery.core.domain.usecase.user.UserUseCase
import com.example.enursery.core.domain.usecase.vgm.InsertVgmWithUpdateInteractor
import com.example.enursery.core.domain.usecase.vgm.InsertVgmWithUpdateUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryInteractor
import com.example.enursery.core.domain.usecase.vgm.VgmHistoryUseCase
import com.example.enursery.core.domain.usecase.vgm.VgmInteractor
import com.example.enursery.core.domain.usecase.vgm.VgmUseCase
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
            vgmHistoryDao = database.vgmHistoryDao(),
            barisDao = database.barisDao()
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

    fun provideInsertVgmWithUpdateRepository(context: Context): IInsertVgmWithUpdateRepository {
        return InsertVgmWithUpdateRepository(
            provideLocalDataSource(context)
        )
    }

    fun provideBarisRepository(context: Context): IBarisRepository {
        return BarisRepository(provideLocalDataSource(context))
    }

    fun providePlotProgressRepository(context: Context): IPlotProgressRepository {
        return PlotProgressRepository(
            vgmDao = AppDatabase.getInstance(context).vgmDao(),
            userSession = provideSessionUseCase(context),
            plotRepository = PlotRepository.getInstance(
                provideRemoteDataSource(context),
                provideLocalDataSource(context),
                provideAppExecutors()
            )
        )
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

    fun provideInsertVgmWithUpdateUseCase(context: Context): InsertVgmWithUpdateUseCase {
        return InsertVgmWithUpdateInteractor(provideInsertVgmWithUpdateRepository(context))
    }

    fun provideBarisUseCase(context: Context): BarisUseCase {
        return BarisInteractor(provideBarisRepository(context))
    }

    fun providePlotProgressUseCase(context: Context): PlotProgressUseCase {
        return PlotProgressInteractor(providePlotProgressRepository(context))
    }

}


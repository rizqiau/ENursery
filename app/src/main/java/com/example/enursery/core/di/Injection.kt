package com.example.enursery.core.di

import android.content.Context
import com.example.enursery.core.data.source.MasterDataSeeder
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.local.room.AppDatabase
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.repository.PlotRepository
import com.example.enursery.core.data.source.repository.UserRepository
import com.example.enursery.core.data.source.repository.VgmRepository
import com.example.enursery.core.domain.repository.IUserRepository
import com.example.enursery.core.domain.usecase.PlotInteractor
import com.example.enursery.core.domain.usecase.PlotUseCase
import com.example.enursery.core.domain.usecase.UserInteractor
import com.example.enursery.core.domain.usecase.UserUseCase
import com.example.enursery.core.domain.usecase.VgmInteractor
import com.example.enursery.core.domain.usecase.VgmUseCase
import com.example.enursery.core.utils.AppExecutors
import com.example.enursery.core.utils.JsonHelper

object Injection {

    fun provideSeeder(context: Context): MasterDataSeeder {
        val database = AppDatabase.getInstance(context)
        val jsonHelper = JsonHelper(context)
        val remoteDataSource = RemoteDataSource.getInstance(jsonHelper)

        return MasterDataSeeder(
            remoteDataSource = remoteDataSource,
            roleDao = database.roleDao(),
            wilayahDao = database.wilayahDao()
        )
    }

    fun provideRepository(context: Context): IUserRepository {
        val database = AppDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(
            plotDao = database.plotDao(),
            userDao = database.userDao(),
            roleDao = database.roleDao(),
            wilayahKerjaDao = database.wilayahDao(),
            vgmDao = database.vgmDao()
        )
        val appExecutors = AppExecutors()

        return UserRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideUserUseCase(context: Context): UserUseCase {
        val repository = provideRepository(context)
        return UserInteractor(repository)
    }

    fun providePlotUseCase(context: Context): PlotUseCase {
        val database = AppDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(
            plotDao = database.plotDao(),
            userDao = database.userDao(),
            roleDao = database.roleDao(),
            wilayahKerjaDao = database.wilayahDao(),
            vgmDao = database.vgmDao()
        )
        val appExecutors = AppExecutors()

        val repository = PlotRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
        return PlotInteractor(repository)
    }

    fun provideVgmUseCase(context: Context): VgmUseCase {
        val database = AppDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(
            plotDao = database.plotDao(),
            userDao = database.userDao(),
            roleDao = database.roleDao(),
            wilayahKerjaDao = database.wilayahDao(),
            vgmDao = database.vgmDao()
        )
        val appExecutors = AppExecutors()

        val repository = VgmRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
        return VgmInteractor(repository)
    }
}

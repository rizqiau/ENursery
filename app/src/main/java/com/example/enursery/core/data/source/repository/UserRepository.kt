package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.enursery.core.data.source.NetworkBoundResource
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.data.source.local.LocalDataSource
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.data.source.remote.RemoteDataSource
import com.example.enursery.core.data.source.remote.network.ApiResponse
import com.example.enursery.core.data.source.remote.response.UserResponse
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.repository.IUserRepository
import com.example.enursery.core.utils.AppExecutors
import com.example.enursery.core.utils.mapper.UserMapper

class UserRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IUserRepository {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(remoteData, localData, appExecutors)
            }
    }

    override suspend fun saveUser(user: User) {
        val userEntity = UserMapper.mapUserDomainToUserEntity(user)
        localDataSource.saveUser(userEntity)
    }

    override suspend fun getUserByEmail(email: String): User? {
        val userEntity = localDataSource.getUserByEmail(email)
        return userEntity?.let {
            UserMapper.mapUserEntityToDomain(it)
        }
    }

    override fun getUserById(userId: String): LiveData<User> {
        return localDataSource.getUserById(userId).map {
            UserMapper.mapUserEntityToDomain(it)
        }
    }

    fun getAllUsers(): LiveData<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<UserResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return localDataSource.getAllUsers().map {
                    UserMapper.mapUserEntitiesToUserDomain(it)
                }
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getUserData()
            }

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = UserMapper.mapUserResponseToUserEntities(data)
                localDataSource.insertUserList(userList)
            }

        }.asLiveData()
    }

    override suspend fun generateNextUserId(): String {
        val lastUser = localDataSource.getLastUser()
        val lastNumber = lastUser?.idUser?.substring(1)?.toIntOrNull() ?: 0
        return "P%04d".format(lastNumber + 1)
    }

    override fun getAllRoles(): LiveData<List<RoleEntity>> = localDataSource.getAllRoles()
    override fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>> = localDataSource.getAllWilayah()

    fun getRemoteUserData(): LiveData<ApiResponse<List<UserResponse>>> {
        return remoteDataSource.getUserData()
    }
}

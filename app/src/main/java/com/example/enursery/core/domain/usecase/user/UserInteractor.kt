package com.example.enursery.core.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.repository.IUserRepository

class UserInteractor(private val userRepository: IUserRepository) : UserUseCase {

    override suspend fun registerUser(user: User): Result<Unit> {
        return try {
            userRepository.saveUser(user)  // Menyimpan user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateUserId(): String {
        return userRepository.generateNextUserId()
    }

    override fun getAllRoles(): LiveData<List<RoleEntity>> = userRepository.getAllRoles()
    override fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>> = userRepository.getAllWilayah()
    override fun getUserById(userId: String): LiveData<User> {
        return userRepository.getUserById(userId)
    }
}

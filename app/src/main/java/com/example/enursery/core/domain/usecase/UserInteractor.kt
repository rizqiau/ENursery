package com.example.enursery.core.domain.usecase

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

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val user = userRepository.getUserByEmail(email)  // Mengambil user berdasarkan email
            if (user != null && user.password == password) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateUserId(): String {
        return userRepository.generateNextUserId()
    }

    override fun getAllRoles(): LiveData<List<RoleEntity>> = userRepository.getAllRoles()
    override fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>> = userRepository.getAllWilayah()
}

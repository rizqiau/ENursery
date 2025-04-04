package com.example.enursery.core.domain.usecase.login

import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.repository.IUserRepository

class LoginInteractor(
    private val userRepository: IUserRepository
) : LoginUseCase {
    override suspend fun loginUser(email: String, password: String): Result<User> {
        return userRepository.getUserByEmail(email)?.let {
            if (it.password == password) Result.success(it)
            else Result.failure(Exception("Invalid credentials"))
        } ?: Result.failure(Exception("User not found"))
    }
}


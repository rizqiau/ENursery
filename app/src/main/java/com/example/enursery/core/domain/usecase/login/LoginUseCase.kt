package com.example.enursery.core.domain.usecase.login

import com.example.enursery.core.domain.model.User

interface LoginUseCase {
    suspend fun loginUser(email: String, password: String): Result<User>
}

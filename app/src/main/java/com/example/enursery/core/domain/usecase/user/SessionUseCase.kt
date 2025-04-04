package com.example.enursery.core.domain.usecase.user

interface SessionUseCase {
    fun isLoggedIn(): Boolean
    fun getUserId(): String?
    fun getUserName(): String?
    fun getUserRole(): String?
    fun logout()
    fun saveSession(userId: String, userName: String, role: String)
}

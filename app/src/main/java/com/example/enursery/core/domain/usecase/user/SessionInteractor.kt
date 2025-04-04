package com.example.enursery.core.domain.usecase.user

import com.example.enursery.core.domain.repository.ISessionRepository

class SessionInteractor(private val sessionRepo: ISessionRepository) : SessionUseCase {
    override fun isLoggedIn() = sessionRepo.isLoggedIn()
    override fun getUserId() = sessionRepo.getUserId()
    override fun getUserName() = sessionRepo.getUserName()
    override fun getUserRole() = sessionRepo.getUserRole()

    override fun logout() {
        sessionRepo.clearSession()
    }

    override fun saveSession(userId: String, userName: String, role: String) {
        sessionRepo.setLoggedIn(true)
        sessionRepo.setUserId(userId)
        sessionRepo.setUserName(userName)
        sessionRepo.setUserRole(role)
    }
}

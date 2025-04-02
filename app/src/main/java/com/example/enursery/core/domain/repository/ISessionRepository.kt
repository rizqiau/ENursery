package com.example.enursery.core.domain.repository

interface ISessionRepository {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(isLoggedIn: Boolean)
    fun getUserId(): String?
    fun setUserId(userId: String)
    fun getUserName(): String?
    fun setUserName(userName: String)
    fun getUserRole(): String?
    fun setUserRole(role: String)
    fun clearSession()
}
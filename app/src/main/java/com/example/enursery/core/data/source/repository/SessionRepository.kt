package com.example.enursery.core.data.source.repository

import com.example.enursery.core.data.source.local.SharedPreferencesHelper
import com.example.enursery.core.domain.repository.ISessionRepository

class SessionRepository(private val sharedPref: SharedPreferencesHelper) : ISessionRepository {
    override fun isLoggedIn(): Boolean = sharedPref.getLoginStatus()
    override fun setLoggedIn(isLoggedIn: Boolean) = sharedPref.setLoginStatus(isLoggedIn)

    override fun getUserId(): String? = sharedPref.getUserId()
    override fun setUserId(userId: String) = sharedPref.setUserId(userId)

    override fun getUserName(): String? = sharedPref.getUserName()
    override fun setUserName(userName: String) = sharedPref.setUserName(userName)

    override fun getUserRole(): String? = sharedPref.getUserRole()
    override fun setUserRole(role: String) = sharedPref.setUserRole(role)

    override fun clearSession() {
        sharedPref.setLoginStatus(false)
        sharedPref.setUserId("")
        sharedPref.setUserName("")
        sharedPref.setUserRole("")
    }
}
package com.example.enursery.core.data.source.local

import android.content.Context
import android.content.SharedPreferences

typealias Pref = SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPref: Pref = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    fun setLoginStatus(isLoggedIn: Boolean) {
        sharedPref.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    fun getLoginStatus(): Boolean = sharedPref.getBoolean("isLoggedIn", false)

    fun setUserId(userId: String) {
        sharedPref.edit().putString("userId", userId).apply()
    }

    fun getUserId(): String? = sharedPref.getString("userId", null)

    fun setUserName(userName: String) {
        sharedPref.edit().putString("userName", userName).apply()
    }

    fun getUserName(): String? = sharedPref.getString("userName", null)

    fun setUserRole(role: String) {
        sharedPref.edit().putString("userRole", role).apply()
    }

    fun getUserRole(): String? = sharedPref.getString("userRole", null)
}
package com.example.enursery.session

import android.content.Context
import com.example.enursery.core.domain.model.User

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit().apply {
            putString("id", user.id)
            putString("nama", user.nama)
            putString("email", user.email)
            putString("role", user.role)
            putString("wilayahKerja", user.wilayahKerja)
            putString("foto", user.foto)
            apply()
        }
    }

    fun getUser(): User? {
        val id = prefs.getString("id", null) ?: return null
        val nama = prefs.getString("nama", "") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val role = prefs.getString("role", "") ?: ""
        val wilayah = prefs.getString("wilayahKerja", "") ?: ""
        val foto = prefs.getString("foto", "") ?: ""

        return User(
            id = id,
            nama = nama,
            role = role,
            wilayahKerja = wilayah,
            foto = foto,
            email = email,
            password = "" // kosongkan demi keamanan
        )
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

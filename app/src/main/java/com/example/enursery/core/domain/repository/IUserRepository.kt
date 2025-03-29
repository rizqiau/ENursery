package com.example.enursery.core.domain.repository

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.domain.model.User

interface IUserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUserByEmail(email: String): User?
    suspend fun generateNextUserId(): String
    fun getAllRoles(): LiveData<List<RoleEntity>>
    fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>>
}


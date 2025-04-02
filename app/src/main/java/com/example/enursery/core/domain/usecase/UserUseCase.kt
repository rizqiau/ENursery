package com.example.enursery.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.domain.model.User

interface UserUseCase {
    suspend fun registerUser(user: User): Result<Unit>
    suspend fun generateUserId(): String
    fun getAllRoles(): LiveData<List<RoleEntity>>
    fun getAllWilayah(): LiveData<List<WilayahKerjaEntity>>
    fun getUserById(userId: String): LiveData<User>
}

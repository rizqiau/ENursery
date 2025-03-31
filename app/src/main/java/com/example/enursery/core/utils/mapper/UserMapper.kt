package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.UserEntity
import com.example.enursery.core.data.source.remote.response.UserResponse
import com.example.enursery.core.domain.model.User

object UserMapper {
    fun mapUserResponseToUserEntities(input: List<UserResponse>): List<UserEntity> {
        val userList = ArrayList<UserEntity>()
        input.map {
            val user = UserEntity(
                id = it.id,
                nama = it.nama,
                roleId = it.roleId,
                wilayahId = it.wilayahId,
                foto = it.foto,
                email = it.email,
                password = it.password
            )
            userList.add(user)
        }
        return userList
    }

    fun mapUserEntitiesToUserDomain(input: List<UserEntity>): List<User> =
        input.map {
            User(
                id = it.id,
                nama = it.nama,
                role = it.roleId,
                wilayahKerja = it.wilayahId,
                foto = it.foto,
                email = it.email,
                password = it.password
            )
        }


    fun mapUserDomainToUserEntity(input: User) = UserEntity(
        id = input.id,
        nama = input.nama,
        roleId = input.role,
        wilayahId = input.wilayahKerja,
        foto = input.foto,
        email = input.email,
        password = input.password
    )

    fun mapUserEntityToDomain(input: UserEntity): User = User(
        id = input.id,
        nama = input.nama,
        role = input.roleId,
        wilayahKerja = input.wilayahId,
        foto = input.foto,
        email = input.email,
        password = input.password
    )
}
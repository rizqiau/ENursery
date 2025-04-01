package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["id"],
            childColumns = ["roleId"]
        ),
        ForeignKey(
            entity = WilayahKerjaEntity::class,
            parentColumns = ["id"],
            childColumns = ["wilayahId"]
        )
    ],
    indices = [Index("roleId"), Index("wilayahId")]
)
data class UserEntity(
    @PrimaryKey val idUser: String,
    val namaUser: String,
    val roleId: String,
    val wilayahId: String,
    val foto: String,
    val email: String,
    val password: String
)


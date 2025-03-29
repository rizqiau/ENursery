package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class RoleEntity(
    @PrimaryKey val id: String, // R01, R02, dst.
    val name: String            // "Petugas Lapangan", "Supervisor", dll
)
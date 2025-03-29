package com.example.enursery.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wilayah_kerja")
data class WilayahKerjaEntity(
    @PrimaryKey val id: String, // W01, W02, dst.
    val name: String            // "Brahma Binabakti-Mill", dll
)
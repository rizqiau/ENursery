package com.example.enursery.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val idUser: String,
    val namaUser: String,
    val role: String,
    val wilayahKerja: String,
    val foto: String,
    val email: String,
    val password: String
): Parcelable


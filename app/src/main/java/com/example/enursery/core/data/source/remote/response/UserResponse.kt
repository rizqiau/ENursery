package com.example.enursery.core.data.source.remote.response

data class UserResponse (
    val id: String,
    val nama: String,
    val roleId: String,
    val wilayahId: String,
    val foto: String,
    val email: String,
    val password: String
)
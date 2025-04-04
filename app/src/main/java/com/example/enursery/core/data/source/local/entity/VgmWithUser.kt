package com.example.enursery.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class VgmWithUser(
    @Embedded val vgm: VgmEntity,

    @Relation(
        parentColumn = "idUser",
        entityColumn = "idUser"
    )
    val user: UserEntity?
)

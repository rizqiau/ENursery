package com.example.enursery.core.utils

object RoleConstants {
    const val SUPERVISOR = "Supervisor"
    const val ADEL = "Adel"
    const val ASISTEN_KEBUN = "Asisten Kebun"
    const val PETUGAS_LAPANGAN = "Petugas Lapangan"

    val ROLE_CAN_ADD_PLOT = listOf(SUPERVISOR, ADEL, ASISTEN_KEBUN)
}
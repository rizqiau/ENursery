package com.example.enursery.core.utils

fun String.canAddPlot(): Boolean {
    return this in RoleConstants.ROLE_CAN_ADD_PLOT
}

package com.manu.manudesignsystem.components

data class ManuMenuItem(
    val label: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)

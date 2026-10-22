package com.manu.manudesignsystem.components

import androidx.compose.runtime.Composable

data class ManuMenuItem(
    val label: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val leadingIcon: (@Composable () -> Unit)? = null
)
package com.manu.manudesignsystem.components

import androidx.compose.runtime.Composable

data class ManuListItemData(
    val title: String,
    val description: String,
    val leadingContent: @Composable () -> Unit,
    val time: String? = null,
    val onClick: (() -> Unit)? = null,
    val menuItems: List<ManuMenuItem> = emptyList()
)
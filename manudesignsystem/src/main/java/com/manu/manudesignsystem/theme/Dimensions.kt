
package com.manu.manudesignsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ManuDimensions(
    val buttonHeight: Dp = 50.dp,
    val buttonCornerRadius: Dp = 35.dp,
    val cardCornerRadius: Dp = 25.dp,

    val cardPaddingStart: Dp = 10.dp,
    val cardPaddingTop: Dp = 5.dp,
    val cardPaddingEnd: Dp = 10.dp,
    val cardPaddingBottom: Dp = 5.dp,

    val textFieldCornerRadius: Dp = 20.dp
)

object ManuDesignTokens {
    val dimensions = ManuDimensions()
}

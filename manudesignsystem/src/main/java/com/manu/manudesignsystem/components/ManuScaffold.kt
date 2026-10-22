package com.manu.manudesignsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.manu.manudesignsystem.theme.ManuDesignSystem

/**
 * A theme-aware screen layout with optional top and bottom content slots.
 *
 * The content lambda receives padding values for API familiarity. This layout
 * does not automatically apply those values; apply them to the content when needed.
 */
@Composable
fun ManuScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    safeDrawing: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(ManuDesignSystem.spacing.xl),
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .then(
                if (safeDrawing) Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                else Modifier
            )
    ) {
        if (topBar != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                topBar()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content(contentPadding)
        }

        if (bottomBar != null) {
            Box(modifier = Modifier.fillMaxWidth()) {
                bottomBar()
            }
        }
    }
}

/**
 * A simple content wrapper for screens that do not need top/bottom bar slots.
 */
@Composable
fun ManuScreenContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(ManuDesignSystem.spacing.xl),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    scrollable: Boolean = false,
    content: @Composable () -> Unit
) {
    val scrollModifier = if (scrollable) {
        Modifier.verticalScroll(rememberScrollState())
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(contentPadding)
            .then(scrollModifier)
    ) {
        content()
    }
}

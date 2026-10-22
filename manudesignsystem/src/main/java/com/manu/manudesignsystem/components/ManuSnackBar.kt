package com.manu.manudesignsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.manu.manudesignsystem.theme.ManuDesignTokens
import kotlinx.coroutines.delay

enum class ManuSnackbarType {
    Info,
    Success,
    Error
}

@Composable
fun ManuSnackbar(
    message: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    type: ManuSnackbarType = ManuSnackbarType.Info,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    durationMillis: Long = 4_000L
) {
    LaunchedEffect(visible, message, durationMillis) {
        if (visible && durationMillis > 0L) {
            delay(durationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        val colors = MaterialTheme.colorScheme
        val containerColor = when (type) {
            ManuSnackbarType.Info -> colors.inverseSurface
            ManuSnackbarType.Success -> colors.surfaceVariant
            ManuSnackbarType.Error -> colors.errorContainer
        }
        val contentColor = when (type) {
            ManuSnackbarType.Info -> colors.inverseOnSurface
            ManuSnackbarType.Success -> colors.onSurfaceVariant
            ManuSnackbarType.Error -> colors.onErrorContainer
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(ManuDesignTokens.dimensions.cardCornerRadius))
                .background(containerColor)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.width(12.dp))
                ManuTextButton(
                    onClick = {
                        onAction()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = actionLabel,
                        color = contentColor
                    )
                }
            }
        }
    }
}

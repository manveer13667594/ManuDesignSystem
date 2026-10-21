
package com.manu.manudesignsystem.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manu.manudesignsystem.theme.ManuDesignTokens

@Composable
fun ManuButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 24.dp,
        vertical = 14.dp
    ),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressScale = remember { Animatable(1f) }

    LaunchedEffect(interactionSource, enabled) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    if (enabled) {
                        pressScale.animateTo(
                            targetValue = 0.90f,
                            animationSpec = tween(durationMillis = 70)
                        )
                    }
                }

                is PressInteraction.Release,
                is PressInteraction.Cancel -> {
                    pressScale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = 400f
                        )
                    )
                }
            }
        }
    }

    val backgroundColor =
        if (containerColor != Color.Unspecified) {
            containerColor
        } else {
            MaterialTheme.colorScheme.primary
        }

    val foregroundColor =
        if (contentColor != Color.Unspecified) {
            contentColor
        } else {
            MaterialTheme.colorScheme.onPrimary
        }

    val shape = RoundedCornerShape(
        ManuDesignTokens.dimensions.buttonCornerRadius
    )

    Box(
        modifier = modifier
            .scale(pressScale.value)            .defaultMinSize(
                minHeight = ManuDesignTokens.dimensions.buttonHeight
            )
            .clip(shape)
            .background(
                if (enabled) backgroundColor
                else backgroundColor.copy(alpha = 0.38f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(
            value = TextStyle(
                color = foregroundColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            content = content
        )
    }
}

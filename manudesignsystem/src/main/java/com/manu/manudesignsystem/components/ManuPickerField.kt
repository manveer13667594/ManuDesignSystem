package com.manu.manudesignsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

internal val PickerShape = RoundedCornerShape(25.dp)

/**
 * A tappable "choose one" row: a date, a menu entry, a sheet. It is NOT a text input, so it is drawn
 * differently from [ManuTextField]: a filled tile (no outline), no cursor, and a chevron at the end
 * that says "tap to choose". The caller owns the picker state and shows the picker separately.
 *
 * [label] is the small caption above the value (e.g. "Date"). If [value] is empty, [placeholder]
 * is shown in a muted colour. Pass `trailingIcon = null` to hide the chevron.
 *
 * For a list of options that opens out of the field itself, use [ManuDropdownField] instead.
 */
@Composable
fun ManuPickerField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = Icons.Default.KeyboardArrowDown,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingRotation: Float = 0f
) {
    val scheme = MaterialTheme.colorScheme
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { role = Role.Button },
            shape = PickerShape,
            color = scheme.surfaceVariant,
            border = if (isError) BorderStroke(1.dp, scheme.error) else null
        ) {
            PickerRowContent(
                value = value,
                label = label,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                enabled = enabled,
                isError = isError,
                trailingRotation = trailingRotation
            )
        }
        if (supportingText != null) {
            CompositionLocalProvider(
                LocalContentColor provides (if (isError) scheme.error else scheme.onSurfaceVariant)
            ) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    Box(Modifier.padding(start = 16.dp, top = 4.dp)) { supportingText() }
                }
            }
        }
    }
}

/** The inside of a picker field (icon, label, value, chevron). Shared with [ManuDropdownField]. */
@Composable
internal fun PickerRowContent(
    value: String,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    enabled: Boolean,
    isError: Boolean,
    trailingRotation: Float = 0f
) {
    val scheme = MaterialTheme.colorScheme
    val contentAlpha = if (enabled) 1f else 0.38f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = scheme.onSurface.copy(alpha = contentAlpha)
            )
            Spacer(Modifier.width(16.dp))
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (label != null) {
                CompositionLocalProvider(
                    LocalContentColor provides
                            (if (isError) scheme.error else scheme.onSurfaceVariant).copy(alpha = contentAlpha)
                ) {
                    ProvideTextStyle(MaterialTheme.typography.labelMedium) { label() }
                }
            }
            if (value.isNotEmpty() || placeholder == null) {
                // A new value fades in over the old one instead of snapping
                Crossfade(targetState = value, animationSpec = tween(180), label = "pickerValue") { shown ->
                    Text(
                        text = shown,
                        style = MaterialTheme.typography.bodyLarge,
                        color = scheme.onSurface.copy(alpha = contentAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                CompositionLocalProvider(
                    LocalContentColor provides scheme.onSurfaceVariant.copy(alpha = contentAlpha)
                ) {
                    ProvideTextStyle(MaterialTheme.typography.bodyLarge) { placeholder() }
                }
            }
        }
        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = scheme.onSurfaceVariant.copy(alpha = contentAlpha),
                modifier = Modifier.rotate(trailingRotation)
            )
        }
    }
}
package com.manu.manudesignsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

/**
 * Menu in the design system's style. Place it next to the thing that opens it, inside the same Box:
 *   Box { ManuPickerField(...); ManuDropdownMenu(...) }
 *
 * [overlapAnchor] (default on): the menu opens on top of the anchor, starting at its top edge,
 * centred horizontally on it, and only as wide as its items need. It is kept inside the screen.
 * Set it to false for the standard "below the anchor, aligned to its start" behaviour.
 */
@Composable
fun ManuDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<ManuMenuItem>,
    modifier: Modifier = Modifier,
    overlapAnchor: Boolean = true
) {
    if (!overlapAnchor) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 6.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            properties = PopupProperties(focusable = true)
        ) {
            ManuMenuItems(items, onDismissRequest)
        }
        return
    }

    if (!expanded) return

    // Room around the menu so its shadow is not cut off by the popup window
    val shadowMargin = 8.dp
    val marginPx = with(LocalDensity.current) { shadowMargin.roundToPx() }
    val positionProvider = remember(marginPx) { CenteredOverAnchor(marginPx) }

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Box(modifier = Modifier.padding(shadowMargin)) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 6.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .widthIn(min = 112.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    ManuMenuItems(items, onDismissRequest)
                }
            }
        }
    }
}

/**
 * Puts the menu's top edge on the anchor's top edge and centres it horizontally on the anchor.
 * It is moved back inside the window if it would stick out. [margin] is the transparent space
 * around the menu (for the shadow), so the visible menu lines up with the anchor.
 */
private class CenteredOverAnchor(private val margin: Int) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = (anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2)
            .coerceIn(0, maxOf(0, windowSize.width - popupContentSize.width))
        val y = (anchorBounds.top - margin)
            .coerceIn(0, maxOf(0, windowSize.height - popupContentSize.height))
        return IntOffset(x, y)
    }
}

@Composable
internal fun ManuMenuItems(
    items: List<ManuMenuItem>,
    onDismissRequest: () -> Unit,
    selectedLabel: String? = null
) {
    items.forEachIndexed { index, item ->
        DropdownMenuItem(
            text = {
                Text(
                    text = item.label,
                    color = if (item.enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                )
            },
            leadingIcon = item.leadingIcon?.let {
                {
                    CompositionLocalProvider(
                        LocalContentColor provides
                                if (item.enabled) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                                }
                    ) {
                        it()
                    }
                }
            },
            onClick = {
                item.onClick()
                onDismissRequest()
            },
            enabled = item.enabled,
            modifier = Modifier.fillMaxWidth()
        )
        // No divider next to the selected option, so it reads as the field's current value
        val nextIsSelected =
            selectedLabel != null && items.getOrNull(index + 1)?.label == selectedLabel
        val thisIsSelected = selectedLabel != null && item.label == selectedLabel
        if (index < items.lastIndex && !nextIsSelected && !thisIsSelected) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
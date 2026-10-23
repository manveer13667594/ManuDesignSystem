package com.manu.manudesignsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
 * A picker field whose options open as a floating card right on top of it.
 *
 * The field stays visible (icon, label, chevron turning up). The card appears over the value line,
 * lined up with the text. [overlap] is how much of the field's height the card covers, counted
 * from the field's bottom edge up: 0.3 (default) = the card covers the bottom 30% of the field and
 * hangs below it, so the label and the value stay visible; 0.5 = it starts at the field's middle;
 * 0 = it starts right below the field; 1 = it starts at the field's top edge. Each step of 0.1 is
 * about 6dp for a normal field.
 * Options are separated by
 * dividers, except next to the selected one. Choosing an option runs its onClick and closes the card.
 * It opens and closes with an animation.
 *
 * You own the state:
 *   var open by remember { mutableStateOf(false) }
 *   ManuDropdownField(
 *       value = category, expanded = open, onExpandedChange = { open = it },
 *       items = categories.map { c -> ManuMenuItem(label = c, onClick = { category = c }) },
 *       label = { Text("Category") }
 *   )
 */
@Composable
fun ManuDropdownField(
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<ManuMenuItem>,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = Icons.Default.KeyboardArrowDown,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    overlap: Float = 0.3f
) {
    val density = LocalDensity.current
    val scheme = MaterialTheme.colorScheme

    // Where the value text starts inside the field: 16dp padding, plus icon (24dp) and gap (16dp).
    val textStart = if (leadingIcon != null) 56.dp else 16.dp
    val shadowMargin = 8.dp
    val positionProvider = remember(density, textStart, overlap) {
        with(density) {
            CardOverAnchor(
                dx = textStart.roundToPx() - shadowMargin.roundToPx(),
                marginPx = shadowMargin.roundToPx(),
                overlap = overlap
            )
        }
    }

    // Drives the open / close animation. The card stays on screen until the close animation is done.
    val openState = remember { MutableTransitionState(false) }
    openState.targetState = expanded
    val cardShown = openState.currentState || openState.targetState

    val chevronTurn by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(250),
        label = "chevron"
    )

    Box(modifier = modifier.fillMaxWidth()) {
        ManuPickerField(
            value = value,
            onClick = { onExpandedChange(!expanded) },
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
            isError = isError,
            supportingText = supportingText,
            trailingRotation = chevronTurn
        )

        if (cardShown) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { onExpandedChange(false) },
                properties = PopupProperties(focusable = true)
            ) {
                Box(modifier = Modifier.padding(shadowMargin)) {
                    AnimatedVisibility(
                        visibleState = openState,
                        enter = fadeIn(tween(180)) +
                                expandVertically(tween(240), expandFrom = Alignment.Top),
                        exit = fadeOut(tween(120)) +
                                shrinkVertically(tween(180), shrinkTowards = Alignment.Top)
                    ) {
                        Surface(
                            shape = PickerShape,
                            color = scheme.surface,
                            tonalElevation = 0.dp,
                            shadowElevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .widthIn(min = 160.dp)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                ManuMenuItems(
                                    items = items,
                                    onDismissRequest = { onExpandedChange(false) },
                                    selectedLabel = value
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Puts the card's left edge [dx] px from the field's left edge, and its top edge at the point where
 * [overlap] of the field's height is covered, measured from the field's bottom edge up (0.5 = the
 * field's vertical middle). [marginPx] is the transparent shadow margin around the card. The card is
 * moved back inside the window if it would stick out.
 */
private class CardOverAnchor(
    private val dx: Int,
    private val marginPx: Int,
    private val overlap: Float
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = (anchorBounds.left + dx)
            .coerceIn(0, maxOf(0, windowSize.width - popupContentSize.width))
        val y = (anchorBounds.bottom - (anchorBounds.height * overlap).toInt() - marginPx)
            .coerceIn(0, maxOf(0, windowSize.height - popupContentSize.height))
        return IntOffset(x, y)
    }
}
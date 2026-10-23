package com.manu.manudesignsystem.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch

/** How far (px) the sheet must be dragged down, or how fast a fling (px/s), to close it. */
private const val DismissDistance = 140f
private const val DismissVelocity = 1200f
private const val OffScreen = 1500f

/**
 * Bottom sheet that slides up over a dimmed screen.
 *
 * - Drag down anywhere on the sheet to close it, including over the content. When the content is
 *   scrollable it scrolls first; once it is at the top, pulling further down moves the sheet.
 *   Dragging back up moves the sheet back before the content scrolls again.
 * - [scrollable]: the content scrolls when it is taller than the screen (default on). Turn it off if
 *   your content already scrolls by itself (a LazyColumn); drag-to-close still works through it.
 *   With [scrollable] off and non-scrolling content, only the handle drags.
 * - The sheet moves up with the keyboard, so text fields inside stay visible.
 * - Tapping the dimmed area dismisses it ([dismissOnOutsideTap]); tapping the sheet itself does not.
 */
@Composable
fun ManuBottomSheet(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    cornerRadius: Dp = 28.dp,
    dragHandle: Boolean = true,
    dismissOnOutsideTap: Boolean = true,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!expanded) return
    val scope = rememberCoroutineScope()
    val currentOnDismiss by rememberUpdatedState(onDismissRequest)
    // How far the sheet is pushed down from its resting place, in px. One value for drag, enter and exit.
    var offsetPx by remember { mutableFloatStateOf(OffScreen) }

    LaunchedEffect(expanded) {
        animate(OffScreen, 0f, animationSpec = tween(220)) { value, _ -> offsetPx = value }
    }

    // Let go of the sheet: close it if it was pulled far or fast enough, otherwise spring back.
    fun release(velocityY: Float) {
        scope.launch {
            if (offsetPx > DismissDistance || velocityY > DismissVelocity) {
                animate(offsetPx, OffScreen, animationSpec = tween(160)) { v, _ -> offsetPx = v }
                currentOnDismiss()
            } else {
                animate(offsetPx, 0f, animationSpec = tween(180)) { v, _ -> offsetPx = v }
            }
        }
    }

    // Connects the content's scrolling to the sheet: leftover downward drag moves the sheet.
    val connection = remember {
        object : NestedScrollConnection {
            private var flinging = false

            // Dragging up while the sheet is pulled down: move the sheet back first.
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val y = available.y
                if (!flinging && y < 0f && offsetPx > 0f && offsetPx < OffScreen / 2) {
                    val moved = maxOf(y, -offsetPx)
                    offsetPx += moved
                    return Offset(0f, moved)
                }
                return Offset.Zero
            }

            // The content was already at the top and there is drag left over: pull the sheet down.
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val y = available.y
                if (!flinging && y > 0f) {
                    offsetPx += y
                    return Offset(0f, y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                flinging = true
                if (offsetPx > 0f) {
                    release(available.y)
                    return available
                }
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                flinging = false
                return Velocity.Zero
            }
        }
    }

    Dialog(
        onDismissRequest = { if (dismissOnOutsideTap) onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Dimmed background: a tap dismisses
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.42f))
                    .then(
                        if (dismissOnOutsideTap) {
                            Modifier.pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                        } else Modifier
                    )
            )

            // The sheet
            Column(
                modifier = modifier
                    .statusBarsPadding()
                    .offset { IntOffset(0, offsetPx.toInt()) }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                    .background(containerColor)
                    // Swallow taps so they do not reach the dimmed background underneath
                    .pointerInput(Unit) { detectTapGestures { } }
                    .nestedScroll(connection)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .imePadding()
                    .padding(bottom = 8.dp)
            ) {
                if (dragHandle) {
                    // The handle area always drags, even when the content cannot scroll.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onVerticalDrag = { change, dragAmount ->
                                        change.consume()
                                        offsetPx = (offsetPx + dragAmount).coerceAtLeast(0f)
                                    },
                                    onDragEnd = { release(0f) },
                                    onDragCancel = { release(0f) }
                                )
                            }
                            .padding(top = 12.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(contentColor.copy(alpha = 0.22f))
                                .padding(horizontal = 18.dp, vertical = 2.dp)
                        )
                    }
                }
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .then(
                                if (scrollable) Modifier.verticalScroll(rememberScrollState())
                                else Modifier
                            ),
                        content = content
                    )
                }
            }
        }
    }
}
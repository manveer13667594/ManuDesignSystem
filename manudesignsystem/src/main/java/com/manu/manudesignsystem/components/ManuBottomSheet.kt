package com.manu.manudesignsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch

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
    content: @Composable () -> Unit
) {
    if (!expanded) return

    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(expanded) {
        offsetY.snapTo(1000f)
        offsetY.animateTo(0f, animationSpec = tween(220))
    }

    Dialog(
        onDismissRequest = {
            if (dismissOnOutsideTap) onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.42f))
                    .then(
                        if (dismissOnOutsideTap) {
                            Modifier.pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.changes.any { it.pressed }) {
                                            onDismissRequest()
                                            break
                                        }
                                    }
                                }
                            }
                        } else Modifier
                    )
            )

            Column(
                modifier = modifier
                    .offset { IntOffset(0, offsetY.value.toInt()) }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                    .background(containerColor)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .pointerInput(Unit) {
                        var totalDrag = 0f
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                totalDrag = (totalDrag + dragAmount).coerceAtLeast(0f)
                                scope.launch { offsetY.snapTo(totalDrag) }
                            },
                            onDragEnd = {
                                if (totalDrag > 140f) {
                                    scope.launch {
                                        offsetY.animateTo(1000f, animationSpec = tween(160))
                                        onDismissRequest()
                                    }
                                } else {
                                    scope.launch { offsetY.animateTo(0f, animationSpec = tween(180)) }
                                }
                                totalDrag = 0f
                            },
                            onDragCancel = {
                                totalDrag = 0f
                                scope.launch { offsetY.animateTo(0f, tween(180)) }
                            }
                        )
                    }
                    .padding(bottom = 8.dp)
            ) {
                if (dragHandle) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                    content()
                }
            }
        }
    }
}

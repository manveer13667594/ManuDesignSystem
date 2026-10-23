package com.manu.manudesignsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Dialog frame: rounded surface, title, scrollable [content], and a row of [buttons] at the bottom.
 * Use this when you need custom buttons (for example three actions). For the usual
 * confirm / cancel pair use [ManuDialog]; for a plain message use [ManuDialogBox].
 *
 * If the buttons do not fit on one line they wrap onto the next.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ManuCustomDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnOutsideClick: Boolean = true,
    buttons: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnOutsideClick
        )
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Content scrolls if it is taller than the screen; the buttons stay visible.
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    content = content
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = { buttons() }
                )
            }
        }
    }
}

/**
 * Dialog with custom [content] and the standard Cancel / Confirm buttons.
 *
 * [destructive] paints the confirm button in the error colour (delete, discard, ...).
 * [confirmEnabled] lets you block confirming until the content is valid.
 * Nothing is shown unless [visible] is true.
 */
@Composable
fun ManuDialog(
    visible: Boolean,
    title: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = "Cancel",
    confirmEnabled: Boolean = true,
    destructive: Boolean = false,
    dismissOnBackPress: Boolean = true,
    dismissOnOutsideClick: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!visible) return
    ManuCustomDialog(
        onDismissRequest = onDismiss,
        title = title,
        modifier = modifier,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnOutsideClick = dismissOnOutsideClick,
        buttons = {
            if (dismissText != null) {
                ManuOutlineButton(onClick = onDismiss) { Text(dismissText) }
            }
            ManuButton(
                onClick = onConfirm,
                enabled = confirmEnabled,
                containerColor = if (destructive) MaterialTheme.colorScheme.error
                else androidx.compose.ui.graphics.Color.Unspecified,
                contentColor = if (destructive) MaterialTheme.colorScheme.onError
                else androidx.compose.ui.graphics.Color.Unspecified
            ) { Text(confirmText) }
        },
        content = content
    )
}

/** Simple message dialog: a title, a message, Cancel and Confirm. */
@Composable
fun ManuDialogBox(
    visible: Boolean,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnOutsideClick: Boolean = true,
    destructive: Boolean = false
) {
    ManuDialog(
        visible = visible,
        title = title,
        confirmText = confirmText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        dismissText = dismissText,
        destructive = destructive,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnOutsideClick = dismissOnOutsideClick
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
package com.manu.manudesignsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import com.manu.manudesignsystem.theme.ManuDesignSystem
import com.manu.manudesignsystem.theme.ManuDesignTokens

@Composable
fun ManuListItem(
    title: String,
    description: String,
    time: String,
    leadingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    menuItems: List<ManuMenuItem> = emptyList(),
) {
    var menuExpanded by remember { mutableStateOf(false) }



    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            ManuDesignTokens.dimensions.cardCornerRadius
        ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = ManuDesignTokens.dimensions.cardPaddingStart,
                    top = ManuDesignTokens.dimensions.cardPaddingTop,
                    end = ManuDesignTokens.dimensions.cardPaddingEnd,
                    bottom = ManuDesignTokens.dimensions.cardPaddingBottom
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent()

            Spacer(Modifier.width(ManuDesignSystem.spacing.md))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)
                Text(time, style = MaterialTheme.typography.labelSmall)
            }

            if (menuItems.isNotEmpty()) {
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        shape = RoundedCornerShape(25.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp,
                        shadowElevation = 6.dp,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        menuItems.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item.label,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                enabled = item.enabled,
                                onClick = {
                                    menuExpanded = false
                                    item.onClick()
                                },
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )

                            if (index < menuItems.lastIndex) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
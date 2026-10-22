package com.manu.manudesignsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manu.manudesignsystem.theme.ManuDesignTokens

@Composable
fun ManuListCard(
    items: List<ManuListItemData>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            ManuDesignTokens.dimensions.cardCornerRadius
        ),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column {
            items.forEachIndexed { index, item ->
                ManuListItem(
                    title = item.title,
                    description = item.description,
                    time = item.time.orEmpty(),
                    leadingContent = item.leadingContent,
                    onClick = item.onClick,
                    menuItems = item.menuItems
                )

                if (index < items.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}
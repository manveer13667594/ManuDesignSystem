package com.manu.manudesignsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Read-only field that opens something when tapped: a date picker, a menu, a sheet.
 * The caller owns the picker state and shows the picker separately.
 *
 * Why the overlay: a text field swallows taps for itself (to focus), so a click handler placed on
 * the field itself often never fires. A transparent box laid over the field receives the tap first.
 *
 * To anchor a dropdown to this field, wrap both in a Box:
 *   Box { ManuPickerField(...); ManuDropdownMenu(...) }
 */
@Composable
fun ManuPickerField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(imageVector = icon, contentDescription = null) }
            },
            trailingIcon = trailingIcon?.let { icon ->
                { Icon(imageVector = icon, contentDescription = null) }
            },
            supportingText = supportingText,
            isError = isError,
            shape = RoundedCornerShape(25.dp),
            colors = manuTextFieldColors()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick
                )
        )
    }
}

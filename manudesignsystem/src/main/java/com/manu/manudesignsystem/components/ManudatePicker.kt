
package com.manu.manudesignsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate

@Composable
fun ManuDatePicker(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    initialSelectedDateMillis: Long? = null,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    title: String = "Select date"
) {
    if (!expanded) return

    var selectedDate by remember(expanded) {
        mutableStateOf(initialSelectedDateMillis?.toManuDate())
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall
                )

                ManuCalendar(
                    selectedStart = selectedDate,
                    selectedEnd = null,
                    onDateClick = { selectedDate = it },
                    minDate = minDateMillis?.toManuDate(),
                    maxDate = maxDateMillis?.toManuDate()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ManuTextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(8.dp))

                    ManuButton(
                        onClick = {
                            selectedDate?.let {
                                onDateSelected(it.toManuMillis())
                                onDismissRequest()
                            }
                        },
                        enabled = selectedDate != null
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}

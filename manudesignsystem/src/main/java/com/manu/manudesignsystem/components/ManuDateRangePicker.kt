
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

@Composable
fun ManuDateRangePicker(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDateRangeSelected: (startMillis: Long, endMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
    initialStartDateMillis: Long? = null,
    initialEndDateMillis: Long? = null,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    title: String = "Select date range"
) {
    if (!expanded) return

    var startDate by remember(expanded) {
        mutableStateOf(initialStartDateMillis?.toManuDate())
    }
    var endDate by remember(expanded) {
        mutableStateOf(initialEndDateMillis?.toManuDate())
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

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        startDate?.toString() ?: "Start date",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        endDate?.toString() ?: "End date",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ManuCalendar(
                    selectedStart = startDate,
                    selectedEnd = endDate,
                    onDateClick = { date ->
                        when {
                            startDate == null || endDate != null || date < startDate!! -> {
                                startDate = date
                                endDate = null
                            }
                            else -> endDate = date
                        }
                    },
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
                            val start = startDate
                            val end = endDate

                            if (start != null && end != null) {
                                onDateRangeSelected(
                                    start.toManuMillis(),
                                    end.toManuMillis()
                                )
                                onDismissRequest()
                            }
                        },
                        enabled = startDate != null && endDate != null
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

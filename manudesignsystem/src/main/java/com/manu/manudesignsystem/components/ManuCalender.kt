package com.manu.manudesignsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

internal fun Long.toManuDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

internal fun LocalDate.toManuMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

@Composable
internal fun ManuCalendar(
    selectedStart: LocalDate?,
    selectedEnd: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    modifier: Modifier = Modifier
) {
    var visibleMonth by remember {
        mutableStateOf(YearMonth.from(selectedStart ?: LocalDate.now()))
    }
    var showYearPicker by remember { mutableStateOf(false) }
    var yearPageStart by remember { mutableIntStateOf(visibleMonth.year - 5) }

    val firstDay = visibleMonth.atDay(1)
    val offset = firstDay.dayOfWeek.value - 1
    val days = visibleMonth.lengthOfMonth()
    val configuration = LocalConfiguration.current
    val locale = remember(configuration) {
        Locale.getDefault()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = visibleMonth.month.getDisplayName(
                        TextStyle.FULL,
                        locale
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = visibleMonth.year.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            yearPageStart = visibleMonth.year - 5
                            showYearPicker = true
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            if (!showYearPicker) {
                IconButton(onClick = { visibleMonth = visibleMonth.minusMonths(1) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month"
                    )
                }
                IconButton(onClick = { visibleMonth = visibleMonth.plusMonths(1) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next month"
                    )
                }
            }
        }

        if (showYearPicker) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select year",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = { yearPageStart -= 12 }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous years"
                    )
                }
                IconButton(onClick = { yearPageStart += 12 }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next years"
                    )
                }
            }

            repeat(3) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(4) { column ->
                        val year = yearPageStart + row * 4 + column
                        val selectedYear = year == visibleMonth.year
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.35f)
                                .padding(3.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (selectedYear) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    visibleMonth = visibleMonth.withYear(year)
                                    showYearPicker = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = year.toString(),
                                color = if (selectedYear) MaterialTheme.colorScheme.surface
                                else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selectedYear) FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            repeat(6) { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { weekday ->
                        val dayNumber = week * 7 + weekday - offset + 1
                        val date = if (dayNumber in 1..days) visibleMonth.atDay(dayNumber) else null
                        val enabled = date != null &&
                                (minDate == null || date >= minDate) &&
                                (maxDate == null || date <= maxDate)
                        val isStart = date != null && date == selectedStart
                        val isEnd = date != null && date == selectedEnd
                        val inRange = date != null && selectedStart != null && selectedEnd != null &&
                                date > selectedStart && date < selectedEnd
                        val selected = isStart || isEnd

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(if (selected) CircleShape else RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        selected -> MaterialTheme.colorScheme.onSurface
                                        inRange -> MaterialTheme.colorScheme.surfaceVariant
                                        else -> androidx.compose.ui.graphics.Color.Transparent
                                    }
                                )
                                .then(
                                    if (enabled) Modifier.clickable { date?.let(onDateClick) }
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = when {
                                        selected -> MaterialTheme.colorScheme.surface
                                        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                        else -> MaterialTheme.colorScheme.onSurface
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

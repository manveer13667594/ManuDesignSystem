package com.manu.designsystemdemoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manu.manudesignsystem.components.ManuButton
import com.manu.manudesignsystem.components.ManuCard
import com.manu.manudesignsystem.components.ManuTextField
import com.manu.manudesignsystem.theme.ManuDesignSystem
import com.manu.manudesignsystem.theme.ManuDesignSystemTheme

@Composable
 fun DemoScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var sampleText by remember { mutableStateOf("") }

    ManuDesignSystemTheme(darkTheme = darkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ManuDesignSystem.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {


                Text(
                    text = "Manu Design System",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Monochrome · One UI inspired",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ManuCard(
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dark mode",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = if (darkMode) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it }
                        )
                    }
                }
                ManuTextField(
                    value = sampleText,
                    onValueChange = { sampleText = it },
                    label = { Text("Your name") },
                    placeholder = { Text("Enter your name") }
                )
                ManuCard(
                    modifier = Modifier.fillMaxWidth(),
                )
                {

                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Component preview",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Rounded shapes, monochrome colors, " +
                                    "and comfortable spacing.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(40.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            ) {}

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Sample component",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Reusable design",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        ManuButton(
                            onClick = { darkMode = !darkMode },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Toggle theme")
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                ComponentShowcase()

                Text(
                    text = "Current theme: " +
                            if (darkMode) "Dark" else "Light",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

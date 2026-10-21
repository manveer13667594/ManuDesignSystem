
package com.manu.designsystemdemoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manu.manudesignsystem.components.ManuButton
import com.manu.manudesignsystem.components.ManuCard
import com.manu.manudesignsystem.components.ManuTextField
import com.manu.manudesignsystem.components.ManuTopBar

@Composable
fun ComponentShowcase() {
    var name by remember { mutableStateOf("") }
    var buttonClicks by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ManuTopBar(title = "Component Library")

        Text(
            text = "Buttons",
            style = MaterialTheme.typography.titleMedium
        )

        ManuButton(
            onClick = { buttonClicks++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pressed $buttonClicks times")
        }

        Text(
            text = "Cards",
            style = MaterialTheme.typography.titleMedium
        )

        ManuCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "ManuCard",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "A reusable card with soft corners and comfortable padding."
            )
        }

        Text(
            text = "Text fields",
            style = MaterialTheme.typography.titleMedium
        )

        ManuTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your name") },
            placeholder = { Text("Enter your name") }
        )

        ManuCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (name.isBlank()) {
                    "Your preview will appear here."
                } else {
                    "Hello, $name!"
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

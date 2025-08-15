package com.loadmill.makerchecker.ui.maker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun MakerScreen(username: String) {
    var amount by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Maker Dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("Create a new funds transfer. View recent transfers and their approval status.")

        Spacer(Modifier.height(16.dp))

        // Vertical form: better for phones
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("Recipient") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { /* TODO: call initiate */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Initiate Transfer")
        }

        Spacer(Modifier.height(16.dp))
        Text("Your Transfers", style = MaterialTheme.typography.titleMedium)
        Divider(Modifier.padding(vertical = 8.dp))
        Text("No transfers yet")
    }
}

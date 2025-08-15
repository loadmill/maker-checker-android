package com.loadmill.makerchecker.ui.checker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckerScreen(username: String) {
    Column(Modifier.padding(24.dp)) {
        Text("Checker Dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("Review and approve or reject pending transfers.")
        Spacer(Modifier.height(16.dp))
        Text("Pending Transfers", style = MaterialTheme.typography.titleMedium)
        Divider(Modifier.padding(vertical = 8.dp))
        Text("No pending transfers.") // placeholder
    }
}

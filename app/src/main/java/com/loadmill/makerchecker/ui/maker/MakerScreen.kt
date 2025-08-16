package com.loadmill.makerchecker.ui.maker

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions          // <-- correct package
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.loadmill.makerchecker.data.Repository
import com.loadmill.makerchecker.data.Transfer
import kotlinx.coroutines.launch

@Composable
fun MakerScreen(
    username: String,
    modifier: Modifier = Modifier
) {
    var amount by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var transfers by remember { mutableStateOf(emptyList<Transfer>()) }

    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        Repository.myTransfers().onSuccess { transfers = it }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Maker Dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("Create a new funds transfer. View recent transfers and their approval status.")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("Recipient") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            enabled = !loading && amount.isNotBlank() && recipient.isNotBlank(),
            onClick = {
                if (loading) return@Button
                loading = true
                scope.launch {
                    Repository.initiate(amount, recipient)
                        .onSuccess {
                            amount = ""; recipient = ""
                            Repository.myTransfers().onSuccess { transfers = it }
                            loading = false
                        }
                        .onFailure {
                            loading = false
                            Toast.makeText(
                                ctx, it.message ?: "Failed to create", Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(if (loading) "Submitting..." else "Initiate Transfer")
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text("Your Transfers", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))
        if (transfers.isEmpty()) {
            Text("No transfers yet")
        } else {
            LazyColumn {
                items(transfers) { t ->
                    ListItem(
                        headlineContent = { Text("#${t.transactionId}   $${t.amount} → ${t.recipient}") },
                        supportingContent = { Text("Status: ${t.status}   •   Initiated: ${t.createdAt}") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

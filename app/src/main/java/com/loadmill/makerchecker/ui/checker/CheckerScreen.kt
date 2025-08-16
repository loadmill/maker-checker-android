package com.loadmill.makerchecker.ui.checker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loadmill.makerchecker.data.Repository
import com.loadmill.makerchecker.data.Transfer
import kotlinx.coroutines.launch

@Composable
fun CheckerScreen(
    modifier: Modifier = Modifier
) {
    var pending by remember { mutableStateOf(emptyList<Transfer>()) }
    var busyId by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()

    // initial load (and reload when returning)
    LaunchedEffect(Unit) {
        Repository.pending().onSuccess { pending = it }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Review and approve or reject pending transfers.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Text("Pending Transfers", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        if (pending.isEmpty()) {
            Text("No pending transfers.")
        } else {
            LazyColumn {
                items(pending) { t ->
                    ListItem(
                        headlineContent = {
                            Text("#${t.transactionId}   $${t.amount} → ${t.recipient}")
                        },
                        supportingContent = {
                            Text("By ${t.initiatedBy}  •  ${t.createdAt}  •  ${t.status}")
                        },
                        trailingContent = {
                            Row {
                                OutlinedButton(
                                    enabled = busyId != t.transactionId,
                                    onClick = {
                                        busyId = t.transactionId
                                        scope.launch {
                                            Repository.approve(t.transactionId)
                                                .onSuccess { Repository.pending().onSuccess { pending = it } }
                                                .also { busyId = null }
                                        }
                                    }
                                ) { Text("Approve") }

                                Spacer(Modifier.width(8.dp))

                                OutlinedButton(
                                    enabled = busyId != t.transactionId,
                                    onClick = {
                                        busyId = t.transactionId
                                        scope.launch {
                                            Repository.reject(t.transactionId)
                                                .onSuccess { Repository.pending().onSuccess { pending = it } }
                                                .also { busyId = null }
                                        }
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors()
                                ) { Text("Reject") }
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

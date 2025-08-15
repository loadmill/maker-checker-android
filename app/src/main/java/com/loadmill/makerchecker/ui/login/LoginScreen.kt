package com.loadmill.makerchecker.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (role: String, username: String) -> Unit
) {
    var role by remember { mutableStateOf("maker") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var open by remember { mutableStateOf(false) }

    Column(Modifier.padding(24.dp)) {
        Text("Banking Approval Demo Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Text("Role", style = MaterialTheme.typography.labelLarge)
        ExposedDropdownMenuBox(expanded = open, onExpandedChange = { open = !open }) {
            TextField(
                value = if (role == "maker") "Maker (Finance Officer)" else "Checker (Finance Manager)",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                DropdownMenuItem(text = { Text("Maker (Finance Officer)") }, onClick = {
                    role = "maker"; open = false
                })
                DropdownMenuItem(text = { Text("Checker (Finance Manager)") }, onClick = {
                    role = "checker"; open = false
                })
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Username", style = MaterialTheme.typography.labelLarge)
        TextField(value = username, onValueChange = { username = it }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))
        Text("Password", style = MaterialTheme.typography.labelLarge)
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onLoginSuccess(role, username.ifBlank { role }) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }

        Spacer(Modifier.height(12.dp))
        Text("Demo users:\nmaker / maker1234!\nchecker / checker1234!", style = MaterialTheme.typography.bodySmall)
    }
}

package com.loadmill.makerchecker.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions          // <-- correct package
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.loadmill.makerchecker.data.Repository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoggedIn: (username: String, role: String) -> Unit
) {
    var role by remember { mutableStateOf("maker") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Role", style = MaterialTheme.typography.titleMedium)

        ExposedDropdownMenuBox(
            expanded = menuOpen,
            onExpandedChange = { menuOpen = it },
        ) {
            OutlinedTextField(
                value = if (role == "maker") "Maker (Finance Officer)" else "Checker (Finance Manager)",
                onValueChange = {},
                readOnly = true,
                label = { Text("Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuOpen) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(text = { Text("Maker (Finance Officer)") },
                    onClick = { role = "maker"; menuOpen = false })
                DropdownMenuItem(text = { Text("Checker (Finance Manager)") },
                    onClick = { role = "checker"; menuOpen = false })
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(                    // <-- no FQCN
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(                    // <-- correct import
                keyboardType = KeyboardType.Password
            )
        )

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = !loading,
            onClick = {
                if (loading) return@Button
                loading = true
                scope.launch {
                    Repository.login(username, password, role)
                        .onSuccess {
                            loading = false
                            onLoggedIn(it.username, it.role)
                        }
                        .onFailure {
                            loading = false
                            Toast.makeText(
                                ctx, it.message ?: "Login failed", Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(if (loading) "Logging in..." else "Login")
        }

        Spacer(Modifier.height(24.dp))
        Text("Demo users:\nmaker / maker1234!\nchecker / checker1234!")
    }
}

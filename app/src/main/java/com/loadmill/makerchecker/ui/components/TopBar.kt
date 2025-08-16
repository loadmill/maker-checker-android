package com.loadmill.makerchecker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoTopBar(
    username: String?,
    role: String?,
    onLogout: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Demo Bank", maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            if (!username.isNullOrBlank()) {
                Text(
                    text = "  $username${if (!role.isNullOrBlank()) " ($role)" else ""}  ",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}

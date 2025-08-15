package com.loadmill.makerchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loadmill.makerchecker.ui.checker.CheckerScreen
import com.loadmill.makerchecker.ui.components.DemoTopBar
import com.loadmill.makerchecker.ui.login.LoginScreen
import com.loadmill.makerchecker.ui.maker.MakerScreen
import com.loadmill.makerchecker.ui.theme.MakercheckerandroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MakercheckerandroidTheme { AppRoot() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppRoot() {
    var username by rememberSaveable { mutableStateOf<String?>(null) }
    var role by rememberSaveable { mutableStateOf<String?>(null) }

    // BEFORE LOGIN: render ONLY the login screen
    if (username == null || role == null) {
        LoginScreen { r, u ->
            role = r
            username = u.ifBlank { r }
        }
    } else {
        // AFTER LOGIN: render drawer + scaffold
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Accounts") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } } // fake
                    )
                    NavigationDrawerItem(
                        label = { Text("Transfers") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } } // fake
                    )
                    NavigationDrawerItem(
                        label = { Text("Approvals") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } } // fake
                    )
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                username = null
                                role = null
                            }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    DemoTopBar(
                        username = username,
                        role = role,
                        onLogout = {
                            scope.launch {
                                drawerState.close()
                                username = null
                                role = null
                            }
                        },
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                }
            ) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) {
                    when (role) {
                        "maker" -> MakerScreen(username = username ?: "maker")
                        "checker" -> CheckerScreen(username = username ?: "checker")
                        else -> Text("Unknown role")
                    }
                }
            }
        }
    }
}

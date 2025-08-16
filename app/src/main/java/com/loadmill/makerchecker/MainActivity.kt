package com.loadmill.makerchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
        setContent {
            MakercheckerandroidTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    var username by rememberSaveable { mutableStateOf<String?>(null) }
    var role by rememberSaveable { mutableStateOf<String?>(null) }
    var screen by rememberSaveable { mutableStateOf("login") } // "login" | "maker" | "checker"

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun doLogout() {
        username = null
        role = null
        screen = "login"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Accounts") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Transfers") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Approvals") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        doLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                DemoTopBar(
                    username = username,          // <-- name matches your DemoTopBar
                    role = role,
                    onLogout = ::doLogout,        // (also available from drawer)
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { inner ->
            Box(Modifier.padding(inner).fillMaxSize()) {
                when (screen) {
                    "login" -> LoginScreen(
                        onLoggedIn = { u, r ->
                            username = u
                            role = r
                            screen = if (r == "maker") "maker" else "checker"
                        }
                    )
                    "maker" -> MakerScreen(username = username ?: "")
                    "checker" -> CheckerScreen()
                }
            }
        }
    }
}

package dev.belalkhan.taletree.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.belalkhan.taletree.MainDestination
import dev.belalkhan.taletree.main.home.HomeView
import dev.belalkhan.taletree.main.write.WriteStoryView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import taletree.composeapp.generated.resources.Res
import taletree.composeapp.generated.resources.ic_exit
import taletree.composeapp.generated.resources.ic_write

@Composable
fun MainView(
    viewModel: MainViewModel = koinViewModel()
) {
    val navController: NavHostController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = MainDestination.Root.route,
        modifier = Modifier.fillMaxSize()
    ) {

        composable(MainDestination.Root.route) {
            Main(
                onWrite = { navController.navigate(MainDestination.Write.route) },
                onLogoutRequested = viewModel::onLogoutRequested
            )
        }

        composable(MainDestination.Write.route) {
            WriteStoryView(
                onPublish = { string, string1 -> },
                onSaveDraft = { string, string1 -> }
            )
        }
    }

    if (state.isLogoutDialogVisible) {
        AlertDialog(
            onDismissRequest = viewModel::dismissLogoutDialog,
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = viewModel::onLogout) {
                    Text("Yes", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissLogoutDialog) {
                    Text("Cancel", color = MaterialTheme.colorScheme.secondary)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(
    onWrite: () -> Unit,
    onLogoutRequested: () -> Unit,
) {
    var selectedItem by remember { mutableStateOf<BottomNavDestination>(BottomNavDestination.Home) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedItem.label,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutRequested) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_exit),
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                BottomNavDestination.bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item,
                        onClick = { selectedItem = item },
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWrite) {
                Icon(
                    painter = painterResource(Res.drawable.ic_write),
                    contentDescription = "Add"
                )
            }
        },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                BottomNavDestination.Bookmark -> {

                }

                BottomNavDestination.Home -> {
                    HomeView()
                }

                BottomNavDestination.Profile -> {

                }
            }
        }
    }
}

@Composable
@Preview
private fun MainPreview() {
    Surface {
        Main(
            onWrite = {},
            onLogoutRequested = {}
        )
    }
}


package dev.belalkhan.taletree

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.belalkhan.taletree.auth.AuthView
import dev.belalkhan.taletree.auth.AuthViewModel
import dev.belalkhan.taletree.firebase.auth.AuthState
import dev.belalkhan.taletree.home.HomeView
import dev.belalkhan.taletree.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(
                modules = taleTreeModule
            )
        }
    ) {
        TaleTreeApp()
    }
}

@Composable
private fun TaleTreeApp(
    appViewModel: AppViewModel = koinViewModel<AppViewModel>()
) {
    val state = appViewModel.authState.collectAsStateWithLifecycle()
    val navController: NavHostController = rememberNavController()

    val startDestination = when (state.value) {
        AuthState.Authenticated -> NavDestinations.Home.route
        AuthState.Loading -> NavDestinations.Splash.route
        AuthState.Unauthenticated -> NavDestinations.Login.route
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {

                composable(NavDestinations.Splash.route) {
                    SplashScreen()
                }

                composable(NavDestinations.Login.route) {
                    AuthView()
                }

                composable(NavDestinations.Home.route) {
                    HomeView()
                }
            }
        }
    }
}
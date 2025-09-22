package dev.belalkhan.taletree

sealed class NavDestinations(val route: String) {
    object Login : NavDestinations("login")
    object Home : NavDestinations("home")
    object Splash : NavDestinations("splash")
}
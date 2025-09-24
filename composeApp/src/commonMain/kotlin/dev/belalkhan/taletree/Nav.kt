package dev.belalkhan.taletree

sealed class RootDestination(val route: String) {
    object Login : RootDestination("login")
    object Splash : RootDestination("splash")
    object Main : RootDestination("main")
}

sealed class MainDestination(val route: String) {

    companion object {
        private val PREFIX = "main"
    }

    object Root : MainDestination("$PREFIX/root")
    object Write : MainDestination("$PREFIX/write")
}
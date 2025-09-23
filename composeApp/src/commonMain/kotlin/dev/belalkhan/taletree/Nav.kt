package dev.belalkhan.taletree

sealed class RootDestination(val route: String) {
    object Login : RootDestination("login")
    object Splash : RootDestination("splash")
    object Home : RootDestination("home")
}

sealed class HomeDestination(val route: String) {
    companion object {
        private val PREFIX = "${RootDestination.Home.route}/"
    }

    object Dashboard : HomeDestination("${PREFIX}dashboard")

    object Profile : HomeDestination("${PREFIX}profile/{userId}") {
        fun createRoute(userId: String) = "${PREFIX}profile/$userId"
    }

    object Settings : HomeDestination("${PREFIX}settings")
}
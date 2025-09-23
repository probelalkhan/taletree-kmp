package dev.belalkhan.taletree

import org.jetbrains.compose.resources.DrawableResource
import taletree.composeapp.generated.resources.Res
import taletree.composeapp.generated.resources.ic_bookmark
import taletree.composeapp.generated.resources.ic_exit
import taletree.composeapp.generated.resources.ic_home
import taletree.composeapp.generated.resources.ic_profile

sealed class RootDestination(val route: String) {
    object Login : RootDestination("login")
    object Splash : RootDestination("splash")
    object Main : RootDestination("main")
}

sealed interface MainDestination {
    val route: String
    val icon: DrawableResource
    val label: String

    object Home : MainDestination {
        override val route = "main/home"
        override val icon = Res.drawable.ic_home
        override val label = "Home"
    }

    object Bookmark : MainDestination {
        override val route = "main/bookmarks"
        override val icon = Res.drawable.ic_bookmark
        override val label = "Bookmarks"
    }

    object Profile : MainDestination {
        override val route = "main/profile"
        override val icon = Res.drawable.ic_profile
        override val label = "Profile"
    }

    companion object {
        val bottomNavItems = listOf(Home, Bookmark, Profile)
    }
}

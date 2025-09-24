package dev.belalkhan.taletree.main

import org.jetbrains.compose.resources.DrawableResource
import taletree.composeapp.generated.resources.Res
import taletree.composeapp.generated.resources.ic_bookmark
import taletree.composeapp.generated.resources.ic_home
import taletree.composeapp.generated.resources.ic_profile

sealed interface BottomNavDestination {
    val route: String
    val icon: DrawableResource
    val label: String

    object Home : BottomNavDestination {
        override val route = "main/home"
        override val icon = Res.drawable.ic_home
        override val label = "Home"
    }

    object Bookmark : BottomNavDestination {
        override val route = "main/bookmarks"
        override val icon = Res.drawable.ic_bookmark
        override val label = "Bookmarks"
    }

    object Profile : BottomNavDestination {
        override val route = "main/profile"
        override val icon = Res.drawable.ic_profile
        override val label = "Profile"
    }

    companion object Companion {
        val bottomNavItems = listOf(Home, Bookmark, Profile)
    }
}
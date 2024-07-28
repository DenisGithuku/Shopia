package com.githukudenis.core_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val icon: ImageVector? = null
) {
    object Splash: AppDestination(route = "splash")

    object Login: AppDestination("login")
    object Products: AppDestination(route = "products", icon = Icons.Default.List)
    object ProductDetail: AppDestination(route ="details")
    object CartScreen: AppDestination(route = "cart")

    object ProfileScreen: AppDestination(route = "profile", icon = Icons.Default.Person)

    object About: AppDestination(route = "about")
    object Search: AppDestination(route = "search", icon = Icons.Default.Search)
}

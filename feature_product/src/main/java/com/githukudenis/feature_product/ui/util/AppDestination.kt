package com.githukudenis.feature_product.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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

    object ProfileScreen: AppDestination(route = "profile")
}

package com.githukudenis.coroutinesindustrialbuild.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val icon: ImageVector? = null
) {
    object Splash: AppDestination(route = "splash")
    object Products: AppDestination(route = "products", icon = Icons.Default.List)
    object ProductDetail: AppDestination(route ="details")
}

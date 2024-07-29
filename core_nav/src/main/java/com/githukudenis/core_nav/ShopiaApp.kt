package com.githukudenis.core_nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.githukudenis.core_design.theme.CoroutinesIndustrialBuildTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

@Composable
fun ShopiaApp(
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()
    val snackbarHostState = remember {
        SnackbarHostState()
    }


    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent, darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
    }
    CoroutinesIndustrialBuildTheme {
        val startDestination = if (isLoggedIn) AppDestination.Products else AppDestination.Login


        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, bottomBar = {

            if( navController.currentDestination?.route == AppDestination.Products.route || navController.currentDestination?.route == AppDestination.ProfileScreen.route || navController.currentDestination?.route == AppDestination.Search.route
            ) {
                val destinations = listOf(
                    AppDestination.Products,
                    AppDestination.Search,
                    AppDestination.ProfileScreen,
                )
                destinations.forEach { destination ->
                    BottomNavigation {
                        BottomNavigationItem(selected = destination.route == navController.currentDestination?.route,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(destination.route) {
                                        saveState = true
                                        inclusive = true
                                    }
                                    restoreState = true
                                }
                            },
                            icon = {
                                if (destination.icon != null) {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.route
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = destination.route.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    }
                                )
                            }
                            )
                    }
                }
            }

        }) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavigator(
                    navController = navController,
                    snackBarHostState = snackbarHostState,
                    afterSplashDestination = startDestination
                )
            }
        }
    }
}
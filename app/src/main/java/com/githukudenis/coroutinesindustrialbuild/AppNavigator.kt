package com.githukudenis.coroutinesindustrialbuild

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.githukudenis.auth.ui.LoginScreen
import com.githukudenis.feature_cart.ui.views.cart.CartScreen
import com.githukudenis.feature_product.ui.util.AppDestination
import com.githukudenis.feature_product.ui.views.SplashScreen
import com.githukudenis.feature_product.ui.views.detail.ProductDetailScreen
import com.githukudenis.feature_product.ui.views.products.ProductsScreen
import com.githukudenis.feature_user.ui.profile.ProfileRoute

@Composable
fun AppNavigator(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    afterSplashDestination: AppDestination
) {
    NavHost(
        navController = navController, startDestination = AppDestination.Splash.route
    ) {
        composable(
            route = AppDestination.Splash.route
        ) {
            SplashScreen {
                navController.navigate(afterSplashDestination.route) {
                    popUpTo(AppDestination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = AppDestination.Login.route) {
            LoginScreen(snackBarHostState = snackbarHostState) {
                navController.navigate(AppDestination.Products.route) {
                    popUpTo(AppDestination.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = AppDestination.Products.route,
        ) {
            ProductsScreen(
                snackbarHostState = snackbarHostState,
                onOpenProfile = {
                    navController.navigate(AppDestination.ProfileScreen.route) {
                        popUpTo(AppDestination.ProfileScreen.route) {
                            saveState = true
                            inclusive = true
                        }
                        restoreState = true
                    }
                }
            ) { productId ->
                navController.navigate(
                    route = AppDestination.ProductDetail.route + "/$productId"
                ) {
                    popUpTo(AppDestination.Products.route)
                }
            }
        }
        composable(
            route = AppDestination.ProductDetail.route + "/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) {
            ProductDetailScreen()
        }
        composable(route = AppDestination.CartScreen.route) {
            CartScreen()
        }

        composable(route = AppDestination.ProfileScreen.route) {
            ProfileRoute(onSignOut = {
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(AppDestination.Products.route) {
                        inclusive = true
                    }
                }
            })
        }

    }
}
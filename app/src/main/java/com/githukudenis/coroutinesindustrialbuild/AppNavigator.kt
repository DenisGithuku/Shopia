package com.githukudenis.coroutinesindustrialbuild

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.githukudenis.auth.ui.LoginScreen
import com.githukudenis.feature_product.ui.util.AppDestination
import com.githukudenis.feature_product.ui.views.SplashScreen
import com.githukudenis.feature_product.ui.views.detail.ProductDetailScreen
import com.githukudenis.feature_product.ui.views.products.ProductsScreen

@Composable
fun AppNavigator(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController, startDestination = AppDestination.Splash.route
    ) {
        composable(
            route = AppDestination.Splash.route
        ) {
            SplashScreen {
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(AppDestination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = AppDestination.Login.route) {
            LoginScreen(snackBarHostState = snackbarHostState) { username ->
                navController.navigate(AppDestination.Products.route + "/$username") {
                    popUpTo(AppDestination.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = AppDestination.Products.route + "/{username}",
            arguments = listOf(
                navArgument(name = "username") {
                    type = NavType.StringType
                }
            )
        ) {
            ProductsScreen(
                snackbarHostState = snackbarHostState
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
    }
}
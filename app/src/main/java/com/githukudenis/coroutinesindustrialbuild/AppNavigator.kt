package com.githukudenis.coroutinesindustrialbuild

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.githukudenis.feature_product.ui.util.AppDestination
import com.githukudenis.feature_product.ui.views.SplashScreen
import com.githukudenis.feature_product.ui.views.detail.ProductDetailScreen
import com.githukudenis.feature_product.ui.views.products.ProductsScreen

@Composable
fun AppNavigator(
    navController: NavHostController
) {
    NavHost(
        navController = navController, startDestination = AppDestination.Splash.route
    ) {
        composable(
            route = AppDestination.Splash.route
        ) {
            SplashScreen {
                navController.navigate(AppDestination.Products.route) {
                    popUpTo(AppDestination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = AppDestination.Products.route
        ) {
            ProductsScreen { productId ->
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
package com.githukudenis.core_nav

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.githukudenis.auth.ui.LoginRoute
import com.githukudenis.feature_cart.ui.views.cart.CartRoute
import com.githukudenis.feature_product.ui.views.SplashRoute
import com.githukudenis.feature_product.ui.views.detail.ProductDetailRoute
import com.githukudenis.feature_product.ui.views.products.ProductsRoute
import com.githukudenis.feature_user.ui.about.AboutRoute
import com.githukudenis.feature_user.ui.profile.ProfileRoute

@Composable
fun AppNavigator(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    afterSplashDestination: AppDestination
) {
    NavHost(
        navController = navController, startDestination = AppDestination.Splash.route
    ) {
        composable(
            route = AppDestination.Splash.route
        ) {
            SplashRoute {
                navController.navigate(afterSplashDestination.route) {
                    popUpTo(AppDestination.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = AppDestination.Login.route) {
            LoginRoute(snackBarHostState = snackBarHostState) {
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
            ProductsRoute(snackbarHostState = snackBarHostState, onOpenProfile = {
                navController.navigate(AppDestination.ProfileScreen.route) {
                    popUpTo(AppDestination.ProfileScreen.route) {
                        saveState = true
                        inclusive = true
                    }
                    restoreState = true
                }
            }, onOpenProductDetails = { productId ->
                navController.navigate(
                    route = AppDestination.ProductDetail.route + "/$productId"
                ) {
                    popUpTo(AppDestination.Products.route)
                }
            }, onOpenCart = {
                navController.navigate(AppDestination.CartScreen.route) {
                    popUpTo(AppDestination.Products.route) {
                        saveState = true
                    }
                    restoreState = true
                }
            }, onOpenAbout = {
                navController.navigate(AppDestination.About.route) {
                    popUpTo(AppDestination.Products.route) {
                        saveState = true
                    }
                    restoreState = true
                }
            })
        }
        composable(route = AppDestination.ProductDetail.route + "/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.IntType
            })
        ) {
            ProductDetailRoute(snackbarHostState = snackBarHostState)
        }
        composable(route = AppDestination.CartScreen.route) {
            CartRoute(onOpenProductDetails = { productId ->
                navController.navigate(AppDestination.ProductDetail.route + "/${productId}") {
                    popUpTo(AppDestination.CartScreen.route)
                }
            })
        }

        composable(route = AppDestination.ProfileScreen.route) {
            ProfileRoute(onSignOut = {
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(AppDestination.Products.route) {
                        inclusive = true
                    }
                }
            }, snackbarHostState = snackBarHostState)
        }

        composable(route = AppDestination.About.route) {
            AboutRoute()
        }

    }
}
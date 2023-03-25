package com.githukudenis.coroutinesindustrialbuild

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.githukudenis.core_design.theme.CoroutinesIndustrialBuildTheme
import com.githukudenis.feature_product.ui.util.AppDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userLoggedIn = mainViewModel.uiState.value.userLoggedIn

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember {
                SnackbarHostState()
            }

            val startDestination = if (!userLoggedIn) AppDestination.Login else AppDestination.Products

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
                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AppNavigator(
                            navController = navController,
                            snackbarHostState = snackbarHostState,
                            afterSplashDestination = startDestination
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val appStarted = mainViewModel.uiState.value.appStarted
        if (!appStarted) {
            mainViewModel.updateAppStartStatus(true)
        }
    }
}


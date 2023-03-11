package com.githukudenis.coroutinesindustrialbuild

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkStateObserver
import com.githukudenis.coroutinesindustrialbuild.ui.theme.CoroutinesIndustrialBuildTheme
import com.githukudenis.coroutinesindustrialbuild.ui.views.CountriesScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Remember a SystemUiController
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                // setStatusBarColor() and setNavigationBarColor() also exist

                onDispose {}
            }
            CoroutinesIndustrialBuildTheme {
                Scaffold { paddingValues ->
                    CountriesScreen(modifier = Modifier.padding(paddingValues))
//                    ConnectionStatusScreen(
//                        modifier = Modifier.padding(paddingValues),
//                        context = applicationContext
//                    )
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusScreen(
    context: Context,
    modifier: Modifier = Modifier
) {
    val connectionState = NetworkStateObserver(context)
    val connectionStatus = connectionState.observe()
        .collectAsState(initial = NetworkObserver.ConnectionStatus.UNAVAILABLE)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Network status: ${connectionStatus.value}"
        )
    }

}

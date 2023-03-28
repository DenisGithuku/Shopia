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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.githukudenis.core_data.util.Constants
import com.githukudenis.core_design.theme.CoroutinesIndustrialBuildTheme
import com.githukudenis.feature_product.ui.util.AppDestination
import com.githukudenis.feature_product.workers.RefreshProductsWorker
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
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

                val userLoggedIn =
                    mainViewModel.uiState.collectAsStateWithLifecycle().value.userLoggedIn
                val startDestination =
                    if (!userLoggedIn) AppDestination.Login else AppDestination.Products


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

        WorkManager.getInstance(this).getWorkInfosByTagLiveData(Constants.products_sync_worker)
            .observe(this) { workInfoList ->
                if (workInfoList.isNotEmpty()) {
                    val productsWorker = workInfoList.find { workInfo ->
                        workInfo.tags.contains(Constants.products_sync_worker)
                    }
                    productsWorker?.let { workInfo ->
                        if (workInfo.state != WorkInfo.State.ENQUEUED) {
                            setUpProductsSyncWork()
                        }
                    }
                } else {
                    setUpProductsSyncWork()
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

    private fun setUpProductsSyncWork() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        //set execution to be around 8:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 8)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.MINUTE, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDifference = dueDate.timeInMillis - currentDate.timeInMillis

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).setRequiresDeviceIdle(true).build()

        val refreshProductsWorkRequest =
            OneTimeWorkRequestBuilder<RefreshProductsWorker>().setConstraints(
                constraints
            ).setInitialDelay(timeDifference, TimeUnit.MILLISECONDS)
                .addTag(Constants.products_sync_worker).build()

        WorkManager.getInstance(this).enqueue(refreshProductsWorkRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        val workInfo =
            WorkManager.getInstance(this).getWorkInfosByTagLiveData(Constants.products_sync_worker)
        if (workInfo.hasActiveObservers()) {
            workInfo.removeObservers(this)
        }
    }
}


package com.githukudenis.coroutinesindustrialbuild

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.githukudenis.feature_product.workers.RefreshProductsWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class CoroutinesIndustrialBuildApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).setRequiresDeviceIdle(true).build()

        val refreshProductsWorkRequest =
            PeriodicWorkRequestBuilder<RefreshProductsWorker>(12, TimeUnit.HOURS).setConstraints(
                    constraints
                ).build()

        WorkManager.getInstance(this)
            .enqueue(refreshProductsWorkRequest)

    }
}
package com.githukudenis.coroutinesindustrialbuild

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.githukudenis.feature_product.workers.RefreshProductsWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class CoroutinesIndustrialBuildApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


    }

    private fun setUpWork() {
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
            ).setInitialDelay(timeDifference, TimeUnit.MILLISECONDS).build()

        WorkManager.getInstance(this).enqueue(refreshProductsWorkRequest)
    }
}
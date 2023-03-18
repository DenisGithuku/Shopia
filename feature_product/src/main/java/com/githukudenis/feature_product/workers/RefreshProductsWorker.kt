package com.githukudenis.feature_product.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.githukudenis.feature_product.domain.repo.ProductsRepo

class RefreshProductsWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val productsRepo: ProductsRepo
    ): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            productsRepo.refreshProducts()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
package com.githukudenis.feature_product.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.githukudenis.feature_product.domain.repo.ProductsRepository

class RefreshProductsWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val productsRepository: ProductsRepository
    ): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            productsRepository.refreshProducts()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
package com.githukudenis.coroutinesindustrialbuild.data.remote

import com.githukudenis.coroutinesindustrialbuild.data.local.ProductsDao
import com.githukudenis.coroutinesindustrialbuild.data.model.toProducts
import timber.log.Timber
import javax.inject.Inject

class ProductsRemoteDatasource @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val productsDao: ProductsDao
) {

    suspend fun getProductCategories() {
        try {
            val response = productsApiService.getProductCategories()
            if (response.isSuccessful) {
                val productCategories = response.body()
                productCategories?.let { categories ->
                    productsDao.insertAllCategories(*categories.toTypedArray())
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun refreshProducts() {
        try {
            val response = productsApiService.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body()
                products?.let { productsDTO ->
                    productsDao.insertAllProducts(*productsDTO.map { it.toProducts() }
                        .toTypedArray())
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
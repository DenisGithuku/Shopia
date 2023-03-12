package com.githukudenis.coroutinesindustrialbuild.data.remote

import com.githukudenis.coroutinesindustrialbuild.data.local.ProductsDao
import com.githukudenis.coroutinesindustrialbuild.data.model.toProducts
import timber.log.Timber
import javax.inject.Inject

class ProductsRemoteDatasource @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val productsDao: ProductsDao
) {
   suspend fun refreshProducts() {
       try {
           val response = productsApiService.getAllProducts()
           if (response.isSuccessful) {
               val products = response.body()
               products?.let { productsDTO ->
                   productsDao.insertAll(*productsDTO.map { it.toProducts() }.toTypedArray())
               }
           }
       } catch (e: Exception) {
           Timber.e(e)
       }
   }
}
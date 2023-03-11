package com.githukudenis.coroutinesindustrialbuild.data.repo

import android.util.Log
import com.githukudenis.coroutinesindustrialbuild.data.api.ProductsApiService
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategories
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class ProductsDatasource @Inject constructor(
    private val productsApiService: ProductsApiService
) : ProductsRepo {
    override suspend fun getCategories(): Flow<Resource<ProductCategories>> = flow {
        try {
            emit(Resource.Loading())
            val response = productsApiService.getProductCategories()
            if (response.isSuccessful) {
                val categories = response.body()
                emit(Resource.Success(categories))
            }
        }catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    override suspend fun getProducts(): Flow<Resource<List<ProductsDTOItem>>> = flow {
        try {
            emit(Resource.Loading())
            val response = productsApiService.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body()?.toList()
                emit(Resource.Success(products))
            }
        } catch (e: Exception) {
            emit(Resource.Error(cause = e.message.toString()))
            Timber.e(e.message.toString())
        }
    }
        .distinctUntilChanged()
        .retry(retries = 1)
        .flowOn(Dispatchers.IO)

    override suspend fun getProductDetails(productId: Int): Flow<Resource<ProductsDTOItem>> = flow {
        try {
            emit(Resource.Loading())
            val response = productsApiService.getProductDetails(productId)
            if (response.isSuccessful) {
                val productDetails = response.body()
                emit(Resource.Success(productDetails))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}
package com.githukudenis.coroutinesindustrialbuild.domain.repo

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO
import kotlinx.coroutines.flow.Flow

interface ProductsRepo {

    suspend fun getCategories(): Flow<List<ProductCategory>>

    suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>>
    suspend fun getProducts(): Flow<List<ProductDBO>>
    suspend fun getProductDetails(productId: Int): Flow<ProductDBO>
}

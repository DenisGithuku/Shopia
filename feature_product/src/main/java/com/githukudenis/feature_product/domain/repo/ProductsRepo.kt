package com.githukudenis.feature_product.domain.repo

import com.githukudenis.feature_product.data.model.ProductCategory
import com.githukudenis.feature_product.domain.model.ProductDBO
import kotlinx.coroutines.flow.Flow

interface ProductsRepo {

    suspend fun getCategories(): Flow<List<ProductCategory>>

    suspend fun refreshProducts()
    suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>>
    suspend fun getProducts(): Flow<List<ProductDBO>>
    suspend fun getProductDetails(productId: Int): Flow<ProductDBO>
}

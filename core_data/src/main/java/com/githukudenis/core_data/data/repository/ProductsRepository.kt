package com.githukudenis.core_data.data.repository

import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getCategories(): Flow<List<ProductCategory>>
    suspend fun refreshCategories()
    suspend fun refreshProducts()
    suspend fun getProductsInCategory(category: String): Flow<List<ProductDBO>>
    suspend fun getProducts(): Flow<List<ProductDBO>>
    suspend fun getProductDetails(productId: Int): Flow<ProductDBO>
}

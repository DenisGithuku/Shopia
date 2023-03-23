package com.githukudenis.feature_cart.data.repo

import com.githukudenis.core_data.data.local.db.model.cart.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun getProductsInCart(userId: Int): Flow<List<Product>>

    suspend fun insertProductInCart(product: Product)
}
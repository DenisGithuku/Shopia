package com.githukudenis.feature_cart.data.repo

import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.feature_cart.ui.views.cart.ProductInCart
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun getProductsInCart(userId: Int): Flow<List<ProductInCart>>

    suspend fun insertProductInCart(product: Product)

    suspend fun clearCart()
}
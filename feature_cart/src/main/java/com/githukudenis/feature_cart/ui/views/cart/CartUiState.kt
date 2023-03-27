package com.githukudenis.feature_cart.ui.views.cart

import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO

data class CartUiState(
    val isLoading: Boolean = false,
    val cartState: CartState? = null,
    val userMessages: List<UserMessage> = emptyList()
)

data class CartState(
    val products: List<ProductInCart> = emptyList()
)

data class ProductInCart(
    val quantity: Int? = null,
    val productDBO: ProductDBO? = null
)

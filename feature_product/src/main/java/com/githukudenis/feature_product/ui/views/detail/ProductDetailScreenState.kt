package com.githukudenis.feature_product.ui.views.detail

import com.githukudenis.core_data.util.UserMessage

data class ProductDetailScreenState(
    val product: ProductDetailState = ProductDetailState(),
    val isLoading: Boolean = false,
    val cartState: CartState? = null,
    val userMessages: List<UserMessage> = emptyList()
)

data class ProductDetailState(
    val id: Int? = null,
    val category: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val image: String? = null,
    val rating: Double? = null
)

data class CartState(
    val isLoading: Boolean = false,
    val productCount: Int? = null,
)
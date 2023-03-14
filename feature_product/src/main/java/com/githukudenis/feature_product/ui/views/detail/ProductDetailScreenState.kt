package com.githukudenis.feature_product.ui.views.detail

data class ProductDetailScreenState(
    val product: ProductDetailState = ProductDetailState(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ProductDetailState(
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val image: String? = null,
    val rating: Double? = null
)
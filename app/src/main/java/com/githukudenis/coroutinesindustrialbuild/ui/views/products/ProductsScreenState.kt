package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem

data class ProductsScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val products: List<ProductsDTOItem> = emptyList(),
    val error: String? = null,
    val selectedCategory: String? = null
)
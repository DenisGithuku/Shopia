package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO

data class ProductsScreenState(
    val categoriesLoading: Boolean = false,
    val productsLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val isRefreshing: Boolean = false,
    val products: List<ProductDBO> = emptyList(),
    val error: String? = null,
    val selectedCategory: String? = null
)
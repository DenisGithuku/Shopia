package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategories
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductsDTOItem

data class ProductsScreenState(
    val categoriesLoading: Boolean = false,
    val productsLoading: Boolean = false,
    val categories: ProductCategories? = null,
    val isRefreshing: Boolean = false,
    val products: List<ProductsDTOItem> = emptyList(),
    val error: String? = null,
    val selectedCategory: String? = null
)
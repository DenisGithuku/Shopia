package com.githukudenis.feature_product.ui.views.products

import com.githukudenis.feature_product.data.model.ProductCategory
import com.githukudenis.feature_product.domain.model.ProductDBO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem

data class ProductsScreenState(
    val categoriesLoading: Boolean = false,
    val productsLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val isRefreshing: Boolean = false,
    val products: List<ProductDBO> = emptyList(),
    val error: String? = null,
    val selectedCategory: String? = null,
    val userState: UserState? = null
)

data class UserState(
    val users: List<UsersDTOItem> = emptyList(),
    val currentUser: UsersDTOItem? = null
)
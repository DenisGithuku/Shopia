package com.githukudenis.feature_product.ui.views.products

import com.githukudenis.auth.ui.UserMessage
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
    val userState: UserState? = null,
    val userMessages: List<UserMessage> = emptyList()
)

data class UserState(
    val currentUser: UsersDTOItem? = null,
    val userLoading: Boolean = false,
)
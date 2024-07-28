package com.githukudenis.feature_product.ui.views.products

import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_cart.ui.views.cart.ProductInCart
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem

data class ProductsScreenState(
    val categoriesLoading: Boolean = false,
    val productsLoading: Boolean = false,
    val categories: List<ProductCategory> = emptyList(),
    val isRefreshing: Boolean = false,
    val products: List<ProductDBO> = emptyList(),
    val error: String? = null,
    val selectedCategory: String? = null,
    val username: String? = null,
    val cartState: CartState = CartState(),
    val searchState: SearchState = SearchState(),
    val userMessages: List<UserMessage> = emptyList(),
)

data class CartState(
    val isLoading: Boolean = false,
    val products: List<ProductInCart> = emptyList(),
)

data class SearchState(
    val query: String = ""
) {
    val isActive: Boolean = query.isNotEmpty()
}
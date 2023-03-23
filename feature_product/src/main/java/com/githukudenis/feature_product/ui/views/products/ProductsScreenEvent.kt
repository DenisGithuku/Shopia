package com.githukudenis.feature_product.ui.views.products

sealed class ProductsScreenEvent {
    class ChangeCategory(val category: String): ProductsScreenEvent()
    object RefreshProducts: ProductsScreenEvent()

    class DismissUserMessage(val messageId: Int): ProductsScreenEvent()
}
package com.githukudenis.coroutinesindustrialbuild.ui.views.products

sealed class ProductsScreenEvent {
    class ChangeCategory(category: String): ProductsScreenEvent()
    object RefreshProducts: ProductsScreenEvent()
}
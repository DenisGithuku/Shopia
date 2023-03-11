package com.githukudenis.coroutinesindustrialbuild.ui.views.products

sealed class ProductsScreenEvent {
    class ChangeCategory(val category: String): ProductsScreenEvent()
    object RefreshProducts: ProductsScreenEvent()
}
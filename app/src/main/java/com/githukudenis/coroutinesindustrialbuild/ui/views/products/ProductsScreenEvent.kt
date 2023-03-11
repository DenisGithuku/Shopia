package com.githukudenis.coroutinesindustrialbuild.ui.views.products

sealed class ProductsScreenEvent {
    class OpenProductDetails(val id: Int): ProductsScreenEvent()
    object RefreshProducts: ProductsScreenEvent()
}
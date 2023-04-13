package com.githukudenis.feature_product.ui.views.detail

sealed class ProductDetailEvent {
    class DismissUserMessage(val messageId: Int) : ProductDetailEvent()
    class AddToCart(val quantity: Int) : ProductDetailEvent()
    object RemoveFromCart : ProductDetailEvent()
}
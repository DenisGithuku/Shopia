package com.githukudenis.core_data.data.local.db.model.cart

data class ProductsInCartDTOItem(
    val __v: Int,
    val date: String,
    val id: Int,
    val products: List<Product>,
    val userId: Int
)
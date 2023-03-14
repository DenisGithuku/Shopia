package com.githukudenis.feature_product.data.model

import com.githukudenis.feature_product.domain.model.ProductDBO

data class ProductsDTOItem(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)

fun ProductsDTOItem.toProducts(): ProductDBO {
    return ProductDBO(
        category, description, id, image, price, rating, title
    )
}
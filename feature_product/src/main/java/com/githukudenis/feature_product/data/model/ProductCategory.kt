package com.githukudenis.feature_product.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_categories")
data class ProductCategory(
    @PrimaryKey()
    val value: String
)
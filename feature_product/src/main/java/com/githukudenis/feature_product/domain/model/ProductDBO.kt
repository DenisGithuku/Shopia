package com.githukudenis.feature_product.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.githukudenis.feature_product.data.model.Rating

@Entity(tableName = "products_table")
data class ProductDBO(
    val category: String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val price: Double,
    @Embedded
    val rating: Rating,
    val title: String
)

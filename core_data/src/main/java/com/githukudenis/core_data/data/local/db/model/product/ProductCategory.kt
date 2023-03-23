package com.githukudenis.core_data.data.local.db.model.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_categories")
data class ProductCategory(
    @PrimaryKey()
    val value: String
)
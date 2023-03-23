package com.githukudenis.core_data.data.local.db.model.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val productId: Int,
    val quantity: Int
)
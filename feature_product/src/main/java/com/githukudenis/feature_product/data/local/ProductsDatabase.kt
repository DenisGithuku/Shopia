package com.githukudenis.feature_product.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.githukudenis.feature_product.data.model.ProductCategory
import com.githukudenis.feature_product.domain.model.ProductDBO

@Database(entities = [ProductDBO::class, ProductCategory::class], version = 1, exportSchema = false)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}

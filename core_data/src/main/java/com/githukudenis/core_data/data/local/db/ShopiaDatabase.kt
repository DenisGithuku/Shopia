package com.githukudenis.core_data.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.githukudenis.core_data.data.local.db.model.ProductsDao
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO

@Database(entities = [ProductDBO::class, ProductCategory::class, Product::class], version = 1, exportSchema = false)
abstract class ShopiaDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao

    abstract fun cartDao(): CartDao
}
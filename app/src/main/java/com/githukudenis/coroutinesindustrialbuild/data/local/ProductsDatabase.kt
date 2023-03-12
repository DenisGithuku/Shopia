package com.githukudenis.coroutinesindustrialbuild.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.githukudenis.coroutinesindustrialbuild.domain.ProductDBO

@Database(entities = [ProductDBO::class], version = 1, exportSchema = false)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}

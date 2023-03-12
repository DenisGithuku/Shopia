package com.githukudenis.coroutinesindustrialbuild.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {
    @Insert(entity = ProductDBO::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg products: ProductDBO)

    // convenience testing method
    @Query("SELECT COUNT(id) FROM products_table")
    suspend fun getProductsCount(): Int

    @Query("SELECT * FROM products_table")
    suspend fun getAllProducts(): List<ProductDBO>

    @Query("SELECT * FROM products_table WHERE id like :id")
    suspend fun getProductById(id: Int): ProductDBO
}
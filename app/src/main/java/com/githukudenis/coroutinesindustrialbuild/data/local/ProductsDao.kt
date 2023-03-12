package com.githukudenis.coroutinesindustrialbuild.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githukudenis.coroutinesindustrialbuild.domain.ProductDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {
    @Insert(entity = ProductDBO::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg products: ProductDBO)

    @Query("SELECT * FROM products_table")
    fun getAllProducts(): Flow<List<ProductDBO>>

    @Query("SELECT * FROM products_table WHERE id like :id")
    fun getProductById(id: Int): Flow<ProductDBO>
}
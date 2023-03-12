package com.githukudenis.coroutinesindustrialbuild.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO

@Dao
interface ProductsDao {
    @Insert(entity = ProductCategory::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(vararg categories: ProductCategory)
    @Insert(entity = ProductDBO::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(vararg products: ProductDBO)

    // convenience testing method
    @Query("SELECT COUNT(id) FROM products_table")
    suspend fun getProductsCount(): Int

    @Query("SELECT COUNT(id) FROM product_categories")
    suspend fun getCategoriesCount(): Int

    @Query("SELECT * FROM products_table")
    suspend fun getAllProducts(): List<ProductDBO>

    @Query("SELECT * FROM product_categories")
    suspend fun getAllCategories(): List<ProductCategory>

    @Query("SELECT * FROM products_table WHERE id like :id")
    suspend fun getProductById(id: Int): ProductDBO
}
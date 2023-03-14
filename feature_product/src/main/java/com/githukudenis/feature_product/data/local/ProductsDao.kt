package com.githukudenis.feature_product.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githukudenis.feature_product.data.model.ProductCategory
import com.githukudenis.feature_product.domain.model.ProductDBO

@Dao
interface ProductsDao {
    @Insert(entity = ProductCategory::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<ProductCategory>)
    @Insert(entity = ProductDBO::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductDBO>)

    @Query("SELECT * FROM products_table WHERE category like :category")
    suspend fun getProductsInCategory(category: String): List<ProductDBO>

    // convenience testing method
    @Query("SELECT COUNT(id) FROM products_table")
    suspend fun getProductsCount(): Int

    @Query("SELECT COUNT(value) FROM product_categories")
    suspend fun getCategoriesCount(): Int

    @Query("SELECT * FROM products_table")
    suspend fun getAllProducts(): List<ProductDBO>

    @Query("SELECT * FROM product_categories")
    suspend fun getAllCategories(): List<ProductCategory>

    @Query("SELECT * FROM products_table WHERE id like :id")
    suspend fun getProductById(id: Int): ProductDBO
}
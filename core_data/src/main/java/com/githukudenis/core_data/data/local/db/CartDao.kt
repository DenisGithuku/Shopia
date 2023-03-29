package com.githukudenis.core_data.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githukudenis.core_data.data.local.db.model.cart.Product

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM cart ORDER BY productId")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT COUNT(productId) FROM cart")
    suspend fun getProductCount(): Int

    @Query("DELETE FROM cart")
    suspend fun deleteCart()
}
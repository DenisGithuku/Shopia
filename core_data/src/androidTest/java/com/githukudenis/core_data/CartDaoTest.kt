package com.githukudenis.core_data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.db.ShopiaDatabase
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@RunWith(AndroidJUnit4::class)
class CartDaoTest {


    private lateinit var shopiaDatabase: ShopiaDatabase
    private lateinit var cartDao: CartDao

    @get:Rule
    val instantTaskExecutorRule by lazy { InstantTaskExecutorRule() }

    @Before
    fun setUp() {
        shopiaDatabase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ShopiaDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        cartDao = shopiaDatabase.cartDao()
    }

    @After
    fun tearDown() {
        shopiaDatabase.clearAllTables()
        shopiaDatabase.close()
    }

    @Test
    fun insertProductList() = runTest {
        val products = listOf(
            Product(
                productId = 1,
                quantity = 10
            ),
            Product(
                productId = 2,
                quantity = 14
            ),
            Product(
                productId = 3,
                quantity = 11
            )
        )
        cartDao.insertProducts(products)
        val cartProductCount = cartDao.getProductCount()
        assertThat(cartProductCount).isEqualTo(3)
    }

    @Test
    fun insertOneProduct() = runTest {
        val product = Product(
            productId = 1,
            quantity = 10
        )
        cartDao.insertProduct(product)
        val allProducts = cartDao.getAllProducts()
        assertThat(allProducts).contains(product)
    }

    @Test
    fun deleteCart() = runTest {
        val products = listOf(
            Product(
                productId = 1,
                quantity = 10
            ),
            Product(
                productId = 2,
                quantity = 14
            ),
            Product(
                productId = 3,
                quantity = 11
            )
        )
        cartDao.insertProducts(products)
        cartDao.deleteCart()
        val productCount = cartDao.getProductCount()
        assertThat(productCount).isEqualTo(0)
    }

    @Test
    fun deleteProduct() = runTest {
        val product = Product(
            productId = 3,
            quantity = 11
        )
        cartDao.insertProduct(product)
        cartDao.deleteProduct(product)
        val productCount = cartDao.getProductCount()
        assertThat(productCount).isEqualTo(0)
    }
}
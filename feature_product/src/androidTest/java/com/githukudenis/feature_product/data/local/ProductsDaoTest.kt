package com.githukudenis.feature_product.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.githukudenis.core_data.data.local.db.ShopiaDatabase
import com.githukudenis.core_data.data.local.db.model.ProductsDao
import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.db.model.product.ProductDBO
import com.githukudenis.core_data.data.local.db.model.product.Rating
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductsDaoTest {
    private lateinit var productsDatabase: ShopiaDatabase
    private lateinit var productsDao: ProductsDao

    @get:Rule
    val hiltExecutorRule: InstantTaskExecutorRule by lazy { InstantTaskExecutorRule() }

    @Before
    fun setUp() {
        productsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ShopiaDatabase::class.java
        ).allowMainThreadQueries().build()

        productsDao = productsDatabase.productsDao()
    }

    @After
    fun tearDown() {
        productsDatabase.clearAllTables()
        productsDatabase.close()
    }

    @Test
    fun insertAllProductsTest() = runTest {
        val products = listOf(
            ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 1,
                image = "https::",
                price = 45.6,
                rating = Rating(count = 7, rate = 3.4),
                title = "Gold Chain"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 2,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 3,
                image = "https::",
                price = 23.1,
                rating = Rating(count = 710, rate = 4.5),
                title = "Pedant"
            )
        )
        productsDao.insertAllProducts(products)
        val productsFromDb = productsDao.getProductsCount()
        assert(productsFromDb == 3)
    }

    @Test
    fun getAllProductsTest() = runTest(UnconfinedTestDispatcher()) {
        val products = listOf(
            ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 1,
                image = "https::",
                price = 45.6,
                rating = Rating(count = 7, rate = 3.4),
                title = "Gold Chain"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 2,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 3,
                image = "https::",
                price = 23.1,
                rating = Rating(count = 710, rate = 4.5),
                title = "Pedant"
            )
        )
        productsDao.insertAllProducts(products)
        val productCount = productsDao.getAllProducts().size
        assertThat(productCount == products.size)
    }

    @Test
    fun getProductByIdTest() = runTest(UnconfinedTestDispatcher()) {
        val products = listOf(
            ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 1,
                image = "https::",
                price = 45.6,
                rating = Rating(count = 7, rate = 3.4),
                title = "Gold Chain"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 2,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            )
        )
        productsDao.insertAllProducts(products)
        val secondProductId = productsDao.getProductById(2).id
        assertThat(secondProductId).isEqualTo(2)
    }

    @Test
    fun getProductsInCategory() = runTest(UnconfinedTestDispatcher()) {
        val products = listOf(
            ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 1,
                image = "https::",
                price = 45.6,
                rating = Rating(count = 7, rate = 3.4),
                title = "Gold Chain"
            ), ProductDBO(
                category = "jewelery",
                description = "Fashion",
                id = 2,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            ),
            ProductDBO(
                category = "electronics",
                description = "TV Bracket",
                id = 3,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            ), ProductDBO(
                category = "men's clothing",
                description = "Bomber jacket",
                id = 4,
                image = "https::",
                price = 23.6,
                rating = Rating(count = 4, rate = 12.8),
                title = "Silver necklace"
            )
        )
        productsDao.insertAllProducts(products)
        val jeweleryProducts = productsDao.getProductsInCategory("jewelery")
        assertThat(jeweleryProducts.size).isEqualTo(2)
    }

    @Test
    fun insertProductCategoriesTest() = runTest {
        val categories = listOf(
            ProductCategory(
                value = "bags"
            ),
            ProductCategory(
                value = "shoes"
            ),
            ProductCategory(
                value = "women's clothing"
            ),
            ProductCategory(
                value = "jewelery"
            )
        )
        productsDao.insertAllCategories(categories)
        val categoryCount = productsDao.getCategoriesCount()
        assertThat(categoryCount).isEqualTo(4)
    }
}
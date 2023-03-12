package com.githukudenis.coroutinesindustrialbuild.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.data.model.Rating
import com.githukudenis.coroutinesindustrialbuild.domain.model.ProductDBO
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
    private lateinit var productsDatabase: ProductsDatabase
    private lateinit var productsDao: ProductsDao

    @get:Rule
    val hiltExecutorRule: InstantTaskExecutorRule by lazy { InstantTaskExecutorRule() }

    @Before
    fun setUp() {
        productsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), ProductsDatabase::class.java
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
        productsDao.insertAllProducts(*products.toTypedArray())
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
        productsDao.insertAllProducts(* products.toTypedArray())
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
        productsDao.insertAllProducts(* products.toTypedArray())
        val secondProductId = productsDao.getProductById(2).id
        assertThat(secondProductId).isEqualTo(2)
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
        productsDao.insertAllCategories(*categories.toTypedArray())
        val categoryCount = productsDao.getCategoriesCount()
        assertThat(categoryCount).isEqualTo(4)
    }
}
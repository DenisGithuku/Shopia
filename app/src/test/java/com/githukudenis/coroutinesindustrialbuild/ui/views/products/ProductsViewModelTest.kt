package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import androidx.test.filters.MediumTest
import com.githukudenis.coroutinesindustrialbuild.data.repo.FakeProductsDataSource
import com.githukudenis.coroutinesindustrialbuild.domain.repo.ProductsRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@MediumTest
class ProductsViewModelTest {

    private lateinit var productsRepo: ProductsRepo
    private lateinit var productsViewModel: ProductsViewModel

    @get:Rule
    val mainDispatcherRule by lazy { MainDispatcherRule() }

    @Before
    fun setUp() {
        productsRepo = FakeProductsDataSource()
        productsViewModel = ProductsViewModel(productsRepo, FakeNetworkObserver())
    }

    @Test
    fun getProductsTest() = runTest(UnconfinedTestDispatcher()) {
        productsViewModel.getProducts()
        val productList = productsViewModel.state.value.products
        assertTrue(productList.isNotEmpty())
    }

    @Test
    fun changeCategoryTest() = runTest(UnconfinedTestDispatcher()) {
        productsViewModel.changeSelectedCategory("jewelery")
        val selectedCategory = productsViewModel.state.value.selectedCategory
        assertEquals("jewelery", selectedCategory)
    }

    @Test
    fun getCategoriesTest() = runTest(UnconfinedTestDispatcher()) {

    }
}
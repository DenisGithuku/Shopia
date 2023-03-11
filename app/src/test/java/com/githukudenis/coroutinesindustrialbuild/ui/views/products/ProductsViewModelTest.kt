package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import androidx.test.filters.MediumTest
import com.githukudenis.coroutinesindustrialbuild.data.repo.FakeProductsDataSource
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class ProductsViewModelTest {
    private lateinit var productsViewModel: ProductsViewModel

    @get:Rule
    val mainDispatcherRule by lazy {  MainDispatcherRule() }

    @Before
    fun setUp() {
        val productsRepo = FakeProductsDataSource()
        productsViewModel = ProductsViewModel(productsRepo, FakeNetworkObserver())
    }

    @Test
    fun getProducts() = runTest(UnconfinedTestDispatcher()) {
        productsViewModel.getProducts()
        val productList = productsViewModel.state.value.products
        assertTrue(productList.isNotEmpty())
    }
}
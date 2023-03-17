package com.githukudenis.feature_product.ui.views.detail

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.githukudenis.feature_product.data.repo.FakeProductsDataSource
import com.githukudenis.feature_product.domain.repo.ProductsRepo
import com.githukudenis.feature_product.ui.views.products.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class ProductsDetailViewModelTest {

    private lateinit var productsRepo: ProductsRepo
    private lateinit var productsDetailViewModel: ProductsDetailViewModel

    @get:Rule
    val hiltRule: MainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        productsRepo = FakeProductsDataSource()
        val savedStateHandle = SavedStateHandle(initialState = mapOf("productId" to 1))
        productsDetailViewModel = ProductsDetailViewModel(productsRepo, savedStateHandle)
    }

    @Test
    fun `get products details with id returns product object`() = runTest {
        productsDetailViewModel.getProductDetails(productId = 1)
        val productDetail = productsDetailViewModel.state.value.product
        assertThat(productDetail.id).isEqualTo(1)
    }
}
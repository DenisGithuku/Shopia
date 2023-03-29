package com.githukudenis.feature_product.ui.views.detail

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.githukudenis.feature_product.data.repo.FakeProductsRepositoryImpl
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.feature_product.ui.views.products.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@MediumTest
class ProductsDetailViewModelTest {

    private lateinit var productsRepository: ProductsRepository
    private lateinit var productsDetailViewModel: ProductsDetailViewModel

    @get:Rule
    val hiltRule: MainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        productsRepository = FakeProductsRepositoryImpl()
        val savedStateHandle = SavedStateHandle(initialState = mapOf("productId" to 1))
        productsDetailViewModel = ProductsDetailViewModel(productsRepository, savedStateHandle)
    }

    @Test
    fun `get products details with id returns product object`() = runTest {
        productsDetailViewModel.getProductDetails(productId = 1)
        val productDetail = productsDetailViewModel.state.value.product
        assertThat(productDetail.id).isEqualTo(1)
    }
}
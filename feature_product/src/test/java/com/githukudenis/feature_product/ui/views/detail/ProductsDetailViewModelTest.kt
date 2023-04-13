package com.githukudenis.feature_product.ui.views.detail

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_product.data.repo.FakeProductsRepositoryImpl
import com.githukudenis.feature_product.ui.views.products.FakeCartRepository
import com.githukudenis.feature_product.ui.views.products.FakeUserPrefsRepository
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
    private lateinit var cartRepository: CartRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @get:Rule
    val hiltRule: MainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        productsRepository = FakeProductsRepositoryImpl()
        cartRepository = FakeCartRepository()
        userPreferencesRepository = FakeUserPrefsRepository()
        val savedStateHandle = SavedStateHandle(initialState = mapOf("productId" to 1))
        productsDetailViewModel = ProductsDetailViewModel(productsRepository, cartRepository, userPreferencesRepository, savedStateHandle)
    }

    @Test
    fun `get products details with id returns product object`() = runTest {
        productsDetailViewModel.getProductDetails(productId = 1)
        val productDetail = productsDetailViewModel.state.value.product
        assertThat(productDetail.id).isEqualTo(1)
    }

    @Test
    fun `insert product into cart`() = runTest {
        val product = Product(productId = 2, quantity = 23)
        productsDetailViewModel.insertProductIntoCart(product)
        val productsInCart = productsDetailViewModel.state.value.cartState
        productsInCart?.let { cartState ->
            cartState.productCount?.let { count ->
                assertThat(count).isEqualTo(1)
            }
        }
    }

    @Test
    fun `delete product from cart`() = runTest {
        val product = Product(productId = 2, quantity = 23)
        productsDetailViewModel.insertProductIntoCart(product)
        productsDetailViewModel.removeFromCart(product.productId)
        val productsInCart = productsDetailViewModel.state.value.cartState
        productsInCart?.let { cartState ->
            cartState.productCount?.let { count ->
                assertThat(count).isEqualTo(0)
            }
        }
    }
}
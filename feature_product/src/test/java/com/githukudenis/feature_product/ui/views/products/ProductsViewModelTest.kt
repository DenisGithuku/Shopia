package com.githukudenis.feature_product.ui.views.products

import androidx.test.filters.MediumTest
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_product.data.repo.FakeProductsRepositoryImpl
import com.githukudenis.feature_user.data.UserRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@MediumTest
class ProductsViewModelTest {

    private lateinit var productsRepository: ProductsRepository
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var userPrefsRepository: UserPreferencesRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val mainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        productsRepository = FakeProductsRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        userPrefsRepository = FakeUserPrefsRepository()
        cartRepository = FakeCartRepository()
        productsViewModel = ProductsViewModel(
            productsRepository, userRepository,
            userPreferencesRepository = userPrefsRepository,
            cartRepository = cartRepository
        )
    }

    @Test
    fun `get all products`() = runTest {
        productsViewModel.getAllProducts()
        val productList = productsViewModel.state.value.products
        assertThat(productList.size).isEqualTo(5)
    }

    @Test
    fun `change category`() = runTest {
        productsViewModel.changeSelectedCategory("jewelery")
        val selectedCategory = productsViewModel.state.value.selectedCategory
        assertThat(selectedCategory).isEqualTo("jewelery")
    }

    @Test
    fun `get categories`() = runTest {
        productsViewModel.getCategories()
        val categories = productsViewModel.state.value.categories
        assertThat(categories.size).isEqualTo(5)
    }

    @Test
    fun `get products in category`() = runTest {
        productsViewModel.getProductsInCategory("jewelery")
        val products = productsViewModel.state.value.products
        assertThat(products.size).isEqualTo(2)
    }

    @Test
    fun `refresh products`() = runTest {
        productsViewModel.getCategories()
        val categories = productsViewModel.state.value.categories
        productsViewModel.changeSelectedCategory(categories.last().value)
        productsViewModel.refreshProducts()
        val productCount = productsViewModel.state.value.products.count {
            it.category == categories.last().value
        }

        assertThat(productCount).isEqualTo(2)
    }

    @Test
    fun `get user by id`() = runTest {
        productsViewModel.getCurrentUserInfo("")
        productsViewModel.state.value.userState?.currentUser?.let { currentUser ->
            assertThat(currentUser.id).isEqualTo(1)
        }
    }

    @Test
    fun `get cart product count`() = runTest {
        productsViewModel.getProductsInCartCount(12)
        val cart = productsViewModel.state.value.cartState
        cart?.let {  cartState ->
            cartState.productCount?.let { count ->
                assertThat(count).isEqualTo(7)
            }
        }
    }
}
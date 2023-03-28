package com.githukudenis.feature_cart.ui.views.cart

import androidx.test.filters.MediumTest
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_product.domain.repo.ProductsRepository
import org.junit.Before
import org.junit.Rule

@MediumTest
class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel
    private lateinit var productsRepository: ProductsRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @get:Rule
    val mainCoroutineRule: MainCoroutineRule by lazy { MainCoroutineRule() }


    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
        productsRepository = FakeProductsRepositoryImpl()
        userPreferencesRepository = FakeUserPrefsRepository()
        cartViewModel = CartViewModel(
            cartRepository, productsRepository,userPreferencesRepository
        )
    }
}
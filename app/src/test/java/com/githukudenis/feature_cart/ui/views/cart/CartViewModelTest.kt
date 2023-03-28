package com.githukudenis.feature_cart.ui.views.cart

import androidx.test.filters.MediumTest
import com.githukudenis.feature_cart.data.repo.CartRepository
import org.junit.Before
import org.junit.Rule

@MediumTest
class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var cartViewModel: CartViewModel

    @get:Rule
    val mainCoroutineRule: MainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        cartRepository =
    }
}
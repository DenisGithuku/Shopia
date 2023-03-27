package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_product.domain.repo.ProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productsRepo: ProductsRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var uiState: MutableState<CartUiState> = mutableStateOf(CartUiState())
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { prefs ->
                val userId = checkNotNull(prefs.userId)
                getProductsInCart(userId)
            }
        }
    }

    suspend fun getProductsInCart(userId: Int) {
        uiState.value = uiState.value.copy(
            isLoading = false
        )
        val productsInCart = combine(
            cartRepository.getProductsInCart(userId), productsRepo.getProducts()
        ) { productsInCart, allProducts ->
            productsInCart.map { productInCart ->
                val productDBO = allProducts.find { dbProductItem ->
                    productInCart.productId == dbProductItem.id
                }
                ProductInCart(
                    productInCart.productId, productDBO = productDBO
                )
            }
        }
        productsInCart.catch {
            val userMessage = UserMessage(id = 0, message = it.message)
            val userMessages = mutableListOf<UserMessage>().apply {
                add(userMessage)
            }
            uiState.value = uiState.value.copy(
                userMessages = userMessages, isLoading = false
            )
        }.collect { products ->
            val cartState = CartState().copy(
                products = products
            )
            uiState.value = uiState.value.copy(
                isLoading = false, cartState = cartState
            )
        }
    }
}
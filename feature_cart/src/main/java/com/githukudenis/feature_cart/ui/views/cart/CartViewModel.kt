package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_cart.data.repo.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var uiState: MutableState<CartUiState> = mutableStateOf(CartUiState())
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { prefs ->
                prefs.userId?.let { userId ->
                    getProductsInCart(userId)
                }
            }
        }
    }

    suspend fun getProductsInCart(userId: Int) {
        uiState.value = uiState.value.copy(
            isLoading = false
        )
        cartRepository.getProductsInCart(userId).catch {
            val userMessage = UserMessage(id = 0, message = it.message)
            val userMessages = mutableListOf<UserMessage>().apply {
                add(userMessage)
            }
            uiState.value = uiState.value.copy(
                userMessages = userMessages, isLoading = false
            )
        }.collectLatest { products ->
            val cartState = uiState.value.cartState.copy(
                products = products
            )
            uiState.value = uiState.value.copy(
                isLoading = false, cartState = cartState
            )
        }
    }

    fun removeItemFromCart(id: Int) {

    }

    fun changeProductCount(count: Int) {
        val cartState = uiState.value.cartState.products.map {

        }
    }
}
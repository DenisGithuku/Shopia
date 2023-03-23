package com.githukudenis.feature_cart.ui.views.cart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.auth.ui.UserMessage
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_product.domain.repo.ProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productsRepo: ProductsRepo
) : ViewModel() {

    var uiState: MutableState<CartUiState> = mutableStateOf(CartUiState())
        private set

    init {

    }

    fun getProductsInCart(userId: Int) = viewModelScope.launch {
        cartRepository.getProductsInCart(userId).catch {
            val userMessage = UserMessage(id = 0, message = it.message)
            val userMessages = mutableListOf<UserMessage>().apply {
                add(userMessage)
            }
            uiState.value = uiState.value.copy(
                userMessages = userMessages
            )
        }.collect { products ->
            productsRepo.getProducts().onEach { productDboList ->
                val productList = productDboList.map { productDBOItem ->
                    products.find { it.productId ==  productDBOItem.id }
                }.map {

                }
                val productsInCart = products.map { product ->
                    ProductInCart(
                        quantity = null, productDBO = null
                    )
                }
                val cartState = CartState().copy(
                    products = productsInCart
                )
            }
        }
    }
}
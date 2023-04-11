package com.githukudenis.feature_product.ui.views.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_cart.data.repo.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsDetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableState<ProductDetailScreenState> = mutableStateOf(
        ProductDetailScreenState()
    )
    val state: State<ProductDetailScreenState> get() = _state

    init {
        val productId: Int = checkNotNull(savedStateHandle["productId"])
        getProductDetails(productId)
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collectLatest { prefs ->
                val userId = checkNotNull(prefs.userId)
                if (userId == -1) {
                    return@collectLatest
                }
                getCartState(userId)
            }
        }
    }

    fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.DismissUserMessage -> {
                val userMessages = _state.value.userMessages.filterNot { message ->
                    message.id == event.messageId
                }
                _state.value = _state.value.copy(
                    userMessages = userMessages
                )
            }

            is ProductDetailEvent.AddToCart -> {
                val id = _state.value.product.id
                val quantity = event.quantity

                id?.let { Product(it, quantity) }?.let { product ->
                    insertProductIntoCart(product)
                }
            }

            ProductDetailEvent.RemoveFromCart -> {
                _state.value.product.id?.let { removeFromCart(it) }
            }
        }
    }


    fun getProductDetails(productId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val productJob = launch {
                productsRepository.getProductDetails(productId).collect { result ->
                    val (category, description, id, image, price, rating, title) = result
                    val productDetailState = ProductDetailState(
                        id = id,
                        category = category,
                        title = title,
                        description = description,
                        price = "$price",
                        image = image,
                        rating = rating.rate
                    )
                    _state.value = _state.value.copy(
                        product = productDetailState
                    )
                }
            }
            productJob.invokeOnCompletion {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun insertProductIntoCart(product: Product) = viewModelScope.launch {
        _state.value = _state.value.copy(
            isLoading = true
        )
        val insertProductJob = launch {
            cartRepository.insertProductInCart(product)
        }

        insertProductJob.invokeOnCompletion { error ->
            val message = UserMessage(id = 0, message = error?.cause?.message)
            val userMessages = mutableListOf<UserMessage>()
            userMessages.add(message)
            if (error != null) {
                _state.value = _state.value.copy(
                    userMessages = userMessages
                )
                return@invokeOnCompletion
            }
            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    private suspend fun getCartState(userId: Int) = viewModelScope.launch {
        val cartState = CartState().copy(
            isLoading = true
        )
        _state.value = _state.value.copy(
            cartState = cartState
        )
        cartRepository.getProductsInCart(userId).catch { throwable ->
            val userMessage = UserMessage(id = 0, message = throwable.message)
            val userMessages = mutableListOf<UserMessage>()
            userMessages.add(userMessage)
            _state.value = _state.value.copy(
                userMessages = userMessages, cartState = cartState.copy(isLoading = false)
            )
        }.collectLatest { products ->
            val productInCart =
                products.any { productInCart -> productInCart.productDBO?.id == _state.value.product.id }
            _state.value = _state.value.copy(
                cartState = cartState.copy(
                    isLoading = false, productCount = products.size
                ), product = _state.value.product.copy(
                    productInCart = productInCart
                )
            )
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }
}
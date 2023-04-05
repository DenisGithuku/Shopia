package com.githukudenis.feature_product.ui.views.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.db.model.cart.Product
import com.githukudenis.core_data.data.local.db.model.product.ProductCategory
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var _state: MutableStateFlow<ProductsScreenState> = MutableStateFlow(
        ProductsScreenState()
    )
    val state: StateFlow<ProductsScreenState> get() = _state

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collectLatest { prefs ->
                val (_, _, userId, username) = prefs

                username?.let { name ->
                    getCurrentUserInfo(name)
                }
                userId?.let { id -> getProductsInCart(id) }

                getCategories()
                getAllProducts()
            }
        }
    }

    fun onEvent(event: ProductsScreenEvent) {
        when (event) {
            is ProductsScreenEvent.RefreshProducts -> {
                refreshProducts()
            }

            is ProductsScreenEvent.ChangeCategory -> {
                if (event.category == _state.value.selectedCategory) {
                    return
                }
                changeSelectedCategory(event.category).also {
                    refreshProducts()
                }
            }

            is ProductsScreenEvent.DismissUserMessage -> {
                refreshUserMessages(event.messageId)
            }

            is ProductsScreenEvent.AddToCart -> {
                val product = Product(productId = event.productId, quantity = 1)
                addProductToCart(product).also {
                    refreshProducts()
                }
            }
        }
    }

    suspend fun getCategories() {
        productsRepository.getCategories().collect { result ->
            val category = ProductCategory(value = "all")
            val categories = result.toMutableList().apply { add(index = 0, category) }
            _state.value = _state.value.copy(
                categories = categories,
                selectedCategory = categories.first().value,
                categoriesLoading = false
            )
        }
    }

    suspend fun getAllProducts() {
        _state.value = _state.value.copy(
            isRefreshing = true
        )
        productsRepository.getProducts().buffer(capacity = 10).collect { result ->
            val products = result.map { product ->
                _state.value.cartState?.products?.any { productInCart -> productInCart.productDBO?.id == product.id }
                    ?.let {
                        ProductState(
                            productInCart = it, product = product
                        )
                    }
            }
            _state.update { state ->
                state.copy(
                    productsLoading = false,
                    isRefreshing = false,
                    products = products as List<ProductState>
                )
            }
        }
    }

    suspend fun getProductsInCategory(category: String) {
        _state.value = _state.value.copy(
            isRefreshing = true
        )
        productsRepository.getProductsInCategory(category).collect { productsInCategory ->
            val products = productsInCategory.map {
                ProductState(
                    product = it
                )
            }
            _state.value = _state.value.copy(
                products = products, isRefreshing = false
            )
        }

    }

    fun changeSelectedCategory(category: String) {
        _state.value = _state.value.copy(
            selectedCategory = category
        )
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _state.value.selectedCategory?.let { category ->
                when (category.lowercase()) {
                    "all" -> {
                        getAllProducts()
                    }

                    else -> {
                        getProductsInCategory(category.lowercase())
                    }
                }
            }
        }
    }

    fun getCurrentUserInfo(username: String) {
        viewModelScope.launch {
            val userState = UserState(
                userLoading = true
            )
            userRepository.getUserByUserName(username).catch {
                val userMessage = UserMessage(id = 0, message = it.message)
                val userMessages = mutableListOf<UserMessage>()
                userMessages.add(userMessage)
                _state.value = _state.value.copy(
                    userMessages = userMessages, userState = userState.copy(userLoading = false)
                )
            }.collect { user ->
                Timber.i(user.toString())
                _state.value = _state.value.copy(
                    userState = userState.copy(
                        currentUser = user, userLoading = false
                    ),
                )
            }
        }
    }

    private fun refreshUserMessages(messageId: Int) {
        val userMessages = _state.value.userMessages.filterNot { userMessage ->
            userMessage.id == messageId
        }
        _state.update { currentState ->
            currentState.copy(userMessages = userMessages)
        }
    }

    suspend fun getProductsInCart(userId: Int) {
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
            _state.value = _state.value.copy(
                cartState = cartState.copy(
                    isLoading = false, products = products
                )
            )
        }
    }

    private fun addProductToCart(
        product: Product
    ) {
        viewModelScope.launch {
            cartRepository.insertProductInCart(product)
        }
    }
}
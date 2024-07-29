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
                _state.update {
                    it.copy(username = prefs.username)
                }

                prefs.userId?.let { id -> getProductsInCart(id) }

                getCategories()
                getAllProducts()
            }
        }
    }

    fun onEvent(event: ProductsScreenEvent) {
        when (event) {
            is ProductsScreenEvent.RefreshProducts -> {
                refreshProducts()
                _state.update {
                    it.copy(searchState = SearchState())
                }
            }

            is ProductsScreenEvent.ChangeCategory -> {
                if (event.category == _state.value.selectedCategory) {
                    return
                }
                changeSelectedCategory(event.category)
                refreshProducts()

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

            is ProductsScreenEvent.OnSearchQueryChange -> {
                val products = _state.value.products.filter {
                    _state.value.searchState.query in it.title || _state.value.searchState.query in it.description
                }

                _state.update { state ->
                    state.copy(searchState = state.searchState.copy(query = event.query),
                        products = products.ifEmpty { _state.value.products })
                }
            }

            ProductsScreenEvent.ClearQuery -> {
                _state.update { state ->
                    state.copy(
                        searchState = state.searchState.copy(query = "")
                    )
                }
            }

            ProductsScreenEvent.Search -> {
                if (!_state.value.searchState.isActive) return
                val products = _state.value.products.filter {
                    _state.value.searchState.query in it.title || _state.value.searchState.query in it.description
                }
                _state.update { state ->
                    state.copy(products = products.ifEmpty { _state.value.products })
                }
            }
        }
    }

    suspend fun getCategories() {
        productsRepository.getCategories().collect { result ->
            val category = ProductCategory(value = "all")
            val categories = result.toMutableList().apply { add(index = 0, category) }
            _state.update { state ->
                state.copy(
                    categories = categories,
                    selectedCategory = categories.first().value,
                    categoriesLoading = false
                )
            }
        }
    }

    suspend fun getAllProducts() {
        _state.value = _state.value.copy(
            isRefreshing = true
        )
        productsRepository.getProducts().buffer(capacity = 10).collect { result ->
            _state.update { state ->
                state.copy(
                    productsLoading = false, isRefreshing = false, products = result
                )
            }
        }
    }

    suspend fun getProductsInCategory(category: String) {
        _state.update { state ->
            state.copy(
                isRefreshing = true
            )
        }
        productsRepository.getProductsInCategory(category).collect { productsInCategory ->
            _state.update { state ->
                state.copy(
                    products = productsInCategory, isRefreshing = false
                )
            }
        }

    }

    fun changeSelectedCategory(category: String) {
        _state.update { state ->
            state.copy(
                selectedCategory = category
            )
        }
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


    private fun refreshUserMessages(messageId: Int) {
        val userMessages = _state.value.userMessages.filterNot { userMessage ->
            userMessage.id == messageId
        }
        _state.update { currentState ->
            currentState.copy(userMessages = userMessages)
        }
    }

    suspend fun getProductsInCart(userId: Int) {
        val cartState = _state.value.cartState.copy(
            isLoading = true
        )

        _state.update { state ->
            state.copy(
                cartState = cartState
            )
        }
        cartRepository.getProductsInCart(userId).catch { throwable ->
            val userMessage = UserMessage(id = 0, message = throwable.message)
            val userMessages = mutableListOf<UserMessage>()
            userMessages.add(userMessage)
            _state.value = _state.value.copy(
                userMessages = userMessages, cartState = cartState.copy(isLoading = false)
            )
        }.collectLatest { products ->
            _state.update { state ->
                state.copy(
                    cartState = cartState.copy(
                        isLoading = false, products = products
                    )
                )
            }
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
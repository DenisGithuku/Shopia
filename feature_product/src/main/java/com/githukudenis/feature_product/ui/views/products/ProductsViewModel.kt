package com.githukudenis.feature_product.ui.views.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.auth.ui.UserMessage
import com.githukudenis.feature_product.data.model.ProductCategory
import com.githukudenis.feature_product.domain.repo.ProductsRepo
import com.githukudenis.feature_user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepo: ProductsRepo,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableStateFlow<ProductsScreenState> = MutableStateFlow(
        ProductsScreenState()
    )
    val state: StateFlow<ProductsScreenState> get() = _state

    init {
        val username: String = checkNotNull(savedStateHandle["username"])
        getCurrentUserInfo(username)
        getCategories()
        getAllProducts()
    }

    fun onEvent(event: ProductsScreenEvent) {
        when (event) {
            is ProductsScreenEvent.RefreshProducts -> {
                refreshProducts()
            }

            is ProductsScreenEvent.ChangeCategory -> {
                changeSelectedCategory(event.category).also {
                    refreshProducts()
                }
            }

            is ProductsScreenEvent.DismissUserMessage -> {
                val userMessages = _state.value.userMessages.filterNot { userMessage ->
                    userMessage.id == event.messageId
                }
                _state.value = _state.value.copy(
                    userMessages = userMessages
                )
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            productsRepo.getCategories().collect { result ->
                val category = ProductCategory(value = "all")
                val categories = result.toMutableList().apply { add(index = 0, category) }
                _state.value = _state.value.copy(
                    categories = categories,
                    selectedCategory = categories.first().value,
                    categoriesLoading = false
                )
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isRefreshing = true
            )
            val countriesDeferred = async {
                productsRepo.getProducts()
            }

            countriesDeferred.await().buffer(capacity = 20).collect { result ->
                _state.update { state ->
                    state.copy(
                        productsLoading = false, isRefreshing = false, products = result
                    )
                }
            }
        }
    }

    fun getProductsInCategory(category: String) = viewModelScope.launch {
        _state.value = _state.value.copy(
            isRefreshing = true
        )
        val productCategoryDeferred = async {
            return@async productsRepo.getProductsInCategory(category)
        }

        productCategoryDeferred.await().collect { productsInCategory ->
            _state.value = _state.value.copy(
                products = productsInCategory, isRefreshing = false
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
            userRepository.getUserByUserName(username)
                .catch {
                    val userMessage = UserMessage(id = 0, message = it.message)
                    val userMessages = mutableListOf<UserMessage>()
                    userMessages.add(userMessage)
                    _state.value = _state.value.copy(
                        userMessages = userMessages,
                        userState = userState.copy(userLoading = false)
                    )
                }
                .collect { user ->
                _state.value = _state.value.copy(
                    userState = userState.copy(
                        currentUser = user,
                        userLoading = false
                    ),
                )
            }
        }
    }
}
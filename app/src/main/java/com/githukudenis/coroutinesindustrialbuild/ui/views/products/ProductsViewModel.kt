package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.coroutinesindustrialbuild.data.model.ProductCategory
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import com.githukudenis.coroutinesindustrialbuild.domain.repo.ProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepo: ProductsRepo,
    private val networkStateObserver: NetworkObserver,
) : ViewModel() {

    private var _state: MutableStateFlow<ProductsScreenState> = MutableStateFlow(
        ProductsScreenState()
    )
    val state: StateFlow<ProductsScreenState> get() = _state

    init {
        viewModelScope.launch {
            networkStateObserver.observe()
                .collect { connectionStatus ->
                    when (connectionStatus) {
                        NetworkObserver.ConnectionStatus.AVAILABLE -> {
                            getCategories()
                            getAllProducts()
                        }

                        NetworkObserver.ConnectionStatus.UNAVAILABLE -> {
                            _state.update { state ->
                                state.copy(error = "Connection unavailable")
                            }
                        }

                        NetworkObserver.ConnectionStatus.LOSING -> {
                            _state.update { state ->
                                state.copy(error = "Poor connection")
                            }
                        }

                        NetworkObserver.ConnectionStatus.LOST -> {
                            _state.update { state ->
                                state.copy(error = "Connection lost. Try again later")
                            }
                        }
                    }
                }
        }
    }

    fun onEvent(event: ProductsScreenEvent) {
        when (event) {
            is ProductsScreenEvent.RefreshProducts -> {
                getAllProducts()
            }

            is ProductsScreenEvent.ChangeCategory -> {
                changeSelectedCategory(event.category)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            productsRepo.getCategories().collect { result ->
                val category = ProductCategory(value = "all")
                val categories = result.toMutableList().apply { add(index = 0, category)}
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
            val countries = async {
                productsRepo.getProducts()
            }

            countries.await()
                .buffer(capacity = 20)
                .collect { result ->
                    _state.update { state ->
                        state.copy(
                            productsLoading = false,
                            isRefreshing = false,
                            products = result
                        )
                    }
                }
        }
    }

    fun getProductsInCategory(category: String) = viewModelScope.launch {
        val productCategoryDeferred = async {
            val productsInCategory = productsRepo.getProductsInCategory(category)
            return@async productsInCategory
        }

        productCategoryDeferred.await().collect() { productsInCategory ->
            _state.value = _state.value.copy(
                products = productsInCategory
            )
        }

    }

    fun changeSelectedCategory(category: String) {
        _state.value = _state.value.copy(
            selectedCategory = category
        )
    }
}
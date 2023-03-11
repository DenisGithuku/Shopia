package com.githukudenis.coroutinesindustrialbuild.ui.views.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.coroutinesindustrialbuild.data.repo.ProductsRepo
import com.githukudenis.coroutinesindustrialbuild.data.repo.Resource
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
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
                            getProducts()
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
                getProducts()
            }
            is ProductsScreenEvent.ChangeCategory -> {
                changeSelectedCategory(event.category)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            productsRepo.getCategories().collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.cause,
                            categoriesLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value.copy(
                            categoriesLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            categories = result.results,
                            selectedCategory = result.results?.let { it.first() },
                            categoriesLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            val countries = async {
                productsRepo.getProducts()
            }

            countries.await()
                .buffer(capacity = 20)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update { state ->
                                state.copy(productsLoading = true)
                            }
                        }

                        is Resource.Success -> {
                            _state.update { state ->
                                state.copy(
                                    productsLoading = false,
                                    isRefreshing = false,
                                    products = result.results ?: emptyList()
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update { state ->
                                state.copy(
                                    productsLoading = false,
                                    error = result.cause
                                )
                            }
                        }
                    }
                }
        }
    }

    fun changeSelectedCategory(category: String) {
        _state.value = _state.value.copy(
            selectedCategory = category
        )
    }
}
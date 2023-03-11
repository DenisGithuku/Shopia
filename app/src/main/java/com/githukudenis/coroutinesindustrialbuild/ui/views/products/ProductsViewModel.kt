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
import timber.log.Timber
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
                            getCountries()
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
            is ProductsScreenEvent.OpenProductDetails -> {
                _state.update { state ->
                    state.copy(
                        selectedProduct = _state.value.products.find { it.id == event.id }
                    )
                }
                Timber.d("${event.id}")
            }

            ProductsScreenEvent.RefreshProducts -> {
                _state.update { state ->
                    state.copy(isRefreshing = true)
                }
                getCountries()
            }
        }
    }

    private fun getCountries() {
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
                                state.copy(isLoading = true)
                            }
                        }

                        is Resource.Success -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    products = result.results ?: emptyList()
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = result.cause
                                )
                            }
                        }
                    }
                }
        }
    }
}
package com.githukudenis.coroutinesindustrialbuild.ui.views.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.coroutinesindustrialbuild.data.repo.ProductsRepo
import com.githukudenis.coroutinesindustrialbuild.data.repo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsDetailViewModel @Inject constructor(
    private val productsRepo: ProductsRepo, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableState<ProductDetailScreenState> = mutableStateOf(
        ProductDetailScreenState()
    )
    val state: State<ProductDetailScreenState> get() = _state

    init {
        val productId: Int = checkNotNull(savedStateHandle["productId"])
        getProductDetails(productId)
    }


    private fun getProductDetails(productId: Int) {
        viewModelScope.launch {
            val productJob = launch {
                productsRepo.getProductDetails(productId)
                    .collect { result ->
                        when(result) {
                            is Resource.Error -> {
                                _state.value = _state.value.copy(
                                    error = result.cause
                                )
                            }
                            is Resource.Loading -> {
                                _state.value = _state.value.copy(
                                    isLoading = true
                                )
                            }
                            is Resource.Success -> {
                                val (category, description, id, image, price, rating, title ) = result.data ?: return@collect
                                val productDetailState = ProductDetailState(
                                    title =  title,
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
                    }
            }

            productJob.invokeOnCompletion {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

}
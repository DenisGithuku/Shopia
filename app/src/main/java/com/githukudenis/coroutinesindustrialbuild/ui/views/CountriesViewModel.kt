package com.githukudenis.coroutinesindustrialbuild.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.coroutinesindustrialbuild.data.repo.CountriesRepo
import com.githukudenis.coroutinesindustrialbuild.data.repo.Resource
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesDatasource: CountriesRepo,
    private val networkStateObserver: NetworkObserver,
) : ViewModel() {

    private var _state: MutableStateFlow<CountriesScreenState> = MutableStateFlow(
        CountriesScreenState()
    )
    val state: StateFlow<CountriesScreenState> get() = _state

    init {
        viewModelScope.launch {
            networkStateObserver.observe().collectLatest { connectionStatus ->
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

    fun onEvent(event: CountriesScreenEvent) {
        when (event) {
            is CountriesScreenEvent.OpenCountriesDetails -> {
                _state.update { state ->
                    state.copy(
                        selectedCountry = _state.value.countries?.find { it.flag == event.flag }
                    )
                }
            }
            CountriesScreenEvent.RefreshCountries -> {
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
                countriesDatasource.getCountries()
            }

            countries.await()
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
                                    countries = result.results
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
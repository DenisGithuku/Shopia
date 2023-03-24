package com.githukudenis.coroutinesindustrialbuild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var uiState = MutableStateFlow(MainActivityUiState())
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collectLatest { prefs ->
                uiState.update { state ->
                    state.copy(
                        userLoggedIn = prefs.userLoggedIn,
                        appStarted = prefs.appInitialized
                    )
                }
            }
        }
    }

    fun updateAppStartStatus(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateAppInitialization(value)
        }
    }
}
package com.githukudenis.coroutinesindustrialbuild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var appStarted = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            appStarted.value = userPreferencesRepository.getAppStartStatus(this)
        }
    }

    fun updateAppStartStatus(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateAppStatusPreference(value)
        }
    }
}
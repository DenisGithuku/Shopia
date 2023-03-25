package com.githukudenis.feature_user.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var uiState = mutableStateOf(ProfileUiState())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.userPreferencesFlow.collect { prefs ->
                val userId = checkNotNull(prefs.userId)
                getUserProfile(userId)
            }
        }
    }

    suspend fun getUserProfile(userId: Int) {
        userRepository.users.collectLatest { usersDTO ->
            usersDTO?.let { allUsers ->
                val user = allUsers.find { user ->
                    user.id == userId
                }
                uiState.value = uiState.value.copy(
                    profile = user
                )
            }
        }
    }

}
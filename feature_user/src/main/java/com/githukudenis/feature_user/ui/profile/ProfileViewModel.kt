package com.githukudenis.feature_user.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var uiState = mutableStateOf(ProfileUiState())
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { prefs ->
                val userId = checkNotNull(prefs.userId)
                getUserProfile(userId)
            }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.Logout -> {
                logout()
            }

            is ProfileUiEvent.DismissUserMessage -> {
                refreshUserMessages(event.messageId)
            }
        }
    }

    suspend fun getUserProfile(userId: Int) {
        uiState.value = uiState.value.copy(
            isLoading = true
        )
        userRepository.users.catch { error ->
                val userMessages = mutableListOf<UserMessage>().apply {
                    val userMessage = UserMessage(id = 0, message = error.message)
                    add(userMessage)
                }
                uiState.value = uiState.value.copy(
                    userMessages = userMessages, isLoading = false
                )
            }.collectLatest { usersDTO ->
                usersDTO?.let { allUsers ->
                    val user = allUsers.find { user ->
                        user.id == userId
                    }
                    uiState.value = uiState.value.copy(
                        profile = user, isLoading = false
                    )
                }
            }
    }

    private fun logout() = viewModelScope.launch {
        cartRepository.clearCart()
        userPreferencesRepository.updateUserLoggedIn(false)
        uiState.value = uiState.value.copy(
            signedOut = true
        )
    }

    private fun refreshUserMessages(messageId: Int) {
        val userMessages = uiState.value.userMessages.filterNot { userMessage ->
            userMessage.id == messageId
        }
        uiState.value = uiState.value.copy(
            userMessages = userMessages
        )
    }
}
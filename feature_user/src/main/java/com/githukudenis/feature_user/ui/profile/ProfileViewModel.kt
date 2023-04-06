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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
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
            userPreferencesRepository.userPreferencesFlow.distinctUntilChanged()
                .collectLatest { prefs ->
                    val userId = checkNotNull(prefs.userId)
                    if (userId == -1) {
                        return@collectLatest
                    }
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
        try {
            cartRepository.clearCart()
            userPreferencesRepository.resetPreferences()
            uiState.value = uiState.value.copy(
                signedOut = true
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
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
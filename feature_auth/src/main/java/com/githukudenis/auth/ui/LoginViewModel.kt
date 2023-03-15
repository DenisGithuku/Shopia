package com.githukudenis.auth.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.auth.api.User
import com.githukudenis.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
    val state: State<LoginUiState> get() = _state

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnUserMessageShown -> {
                updateUserMessages(event.messageId)
            }

            is LoginUiEvent.OnLogin -> {
                login(user = event.user)
            }

            is LoginUiEvent.OnPasswordChange -> {
                changePassword(event.password)
            }

            is LoginUiEvent.OnUsernameChange -> {
                changeUsername(event.username)
            }

            is LoginUiEvent.OnShowLoadingDialog -> {
                toggleLoadingStatus(event.showDialog)
            }

            is LoginUiEvent.OnShowUserMessage -> {
                refreshUserMessages(event.userMessage)
            }

            LoginUiEvent.OnTogglePasswordVisibility -> {
                togglePasswordVisibility()
            }
        }
    }

    private fun togglePasswordVisibility() {
        val passWordVisible = _state.value.formState.passwordIsVisible
        val formState = _state.value.formState.copy(
            passwordIsVisible = !passWordVisible
        )
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    private fun refreshUserMessages(userMessage: UserMessage){
        val userMessages = mutableListOf<UserMessage>()
        userMessages.add(userMessage)
        _state.value = _state.value.copy(
            userMessages = userMessages
        )
    }

    private fun toggleLoadingStatus(isLoading: Boolean) {
        _state.value = _state.value.copy(
            isLoading = isLoading
        )
    }

    private fun changeUsername(value: String) {
        val formState = _state.value.formState.copy(username = value)
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    private fun changePassword(value: String) {
        val formState = _state.value.formState.copy(password = value)
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    private fun updateUserMessages(messageId: Int) {
        val userMessages =
            _state.value.userMessages.filterNot { userMessage -> userMessage.id == messageId }
        _state.value = _state.value.copy(
            userMessages = userMessages
        )
    }

    private fun login(user: User) = viewModelScope.launch {
        val loginDeferred = async {
            return@async authRepository.login(user)
        }
        val loginResult = loginDeferred.await()
        _state.value = _state.value.copy(
            loginSuccess = !loginResult.isNullOrEmpty()
        )

    }
}
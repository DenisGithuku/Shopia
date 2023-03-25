package com.githukudenis.auth.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githukudenis.auth.api.User
import com.githukudenis.auth.data.AuthRepository
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.feature_user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,

    ) : ViewModel() {

    private val _state: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
    val state: State<LoginUiState> get() = _state

    init {
        checkLoginStatus()
    }

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

            is LoginUiEvent.OnShowUserMessage -> {
                refreshUserMessages(event.userMessage)
            }

            LoginUiEvent.OnTogglePasswordVisibility -> {
                togglePasswordVisibility()
            }
        }
    }

    fun togglePasswordVisibility() {
        val passWordVisible = _state.value.formState.passwordIsVisible
        val formState = _state.value.formState.copy(
            passwordIsVisible = !passWordVisible
        )
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    fun refreshUserMessages(userMessage: UserMessage) {
        val userMessages = mutableListOf<UserMessage>()
        userMessages.add(userMessage)
        _state.value = _state.value.copy(
            userMessages = userMessages
        )
    }

    fun changeUsername(value: String) {
        val formState = _state.value.formState.copy(username = value)
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    fun changePassword(value: String) {
        val formState = _state.value.formState.copy(password = value)
        _state.value = _state.value.copy(
            formState = formState
        )
    }

    fun updateUserMessages(messageId: Int) {
        val userMessages =
            _state.value.userMessages.filterNot { userMessage -> userMessage.id == messageId }
        _state.value = _state.value.copy(
            userMessages = userMessages
        )
    }

    fun login(user: User) = viewModelScope.launch {
        _state.value = _state.value.copy(
            isLoading = true
        )
        val loginDeferred = async(context = Dispatchers.IO) {
            return@async authRepository.login(user)
        }
        try {
            val loginResult = loginDeferred.await()
            if (!loginResult.isNullOrEmpty()) {
                /* create handle for fetching
                * user details from the server
                */
                val userNameJob: Job?
                userNameJob = viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        userRepository.getUserByUserName(_state.value.formState.username)
                            .catch {
                                Timber.e(it)
                            }
                            .collect { userDTO ->
                                userDTO?.let { usersDTOItem ->
                                    userPreferencesRepository.storeUserId(usersDTOItem.id)
                                    userPreferencesRepository.storeUserName(usersDTOItem.username)
                                }
                                userPreferencesRepository.updateUserLoggedIn(true)
                            }
                    }
                    val message = UserMessage(id = 0, message = "Logged in successfully")
                    refreshUserMessages(message)
                    _state.value = _state.value.copy(
                        isLoading = false, loginSuccess = true
                    )
                }
                userNameJob.cancel()
            } else {
                val userMessage =
                    UserMessage(id = 0, message = "Could not login. Please check details")
                refreshUserMessages(userMessage)
                _state.value = _state.value.copy(
                    isLoading = false,
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { userPrefs ->
                val isLoggedIn = userPrefs.userLoggedIn
                _state.value = _state.value.copy(
                    loginSuccess = isLoggedIn
                )
            }
        }
    }
}
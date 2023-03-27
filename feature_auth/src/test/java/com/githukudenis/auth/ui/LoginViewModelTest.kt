package com.githukudenis.auth.ui

import androidx.test.filters.MediumTest
import com.githukudenis.auth.api.User
import com.githukudenis.auth.data.AuthRepository
import com.githukudenis.auth.data.FakeAuthRepositoryImpl
import com.githukudenis.auth.data.FakeUserPreferencesRepositoryImpl
import com.githukudenis.core_data.MainCoroutineRule
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_user.data.UserRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@MediumTest
class LoginViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val coroutineRule: MainCoroutineRule by lazy { MainCoroutineRule() }

    @Before
    fun setUp() {
        authRepository = FakeAuthRepositoryImpl()
        userRepository = FakeUserRepositoryImpl()
        userPreferencesRepository = FakeUserPreferencesRepositoryImpl()
        loginViewModel = LoginViewModel(
            authRepository = authRepository,
            userPreferencesRepository = userPreferencesRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `on event`() = runTest {
        val event = LoginUiEvent.OnPasswordChange("12345")
        loginViewModel.onEvent(event)
        val password = loginViewModel.state.value.formState.password
        assertThat(password).isEqualTo("12345")
    }

    @Test
    fun `toggle password visibility`() = runTest {
        val passwordBeforeToggle = loginViewModel.state.value.formState.passwordIsVisible
        loginViewModel.onEvent(LoginUiEvent.OnTogglePasswordVisibility)
        val passwordAfterToggle = loginViewModel.state.value.formState.passwordIsVisible
        assertThat(passwordBeforeToggle).isNotEqualTo(passwordAfterToggle)
    }

    @Test
    fun `refresh user messages`() = runTest {
        val userMessage = UserMessage(id = 1, message = "Example user message")
        loginViewModel.onEvent(LoginUiEvent.OnShowUserMessage(userMessage))
        val userMessagesState = loginViewModel.state.value.userMessages
        assertThat(userMessagesState).contains(userMessage)
    }

    @Test
    fun `change user name`() = runTest {
        loginViewModel.onEvent(LoginUiEvent.OnUsernameChange("sam"))
        val username = loginViewModel.state.value.formState.username
        assertThat(username).isEqualTo("sam")
    }

    @Test
    fun `change password`() = runTest {
        loginViewModel.onEvent(LoginUiEvent.OnPasswordChange("12345"))
        val password = loginViewModel.state.value.formState.password
        assertThat(password).isEqualTo("12345")
    }

    @Test
    fun `update user messages`() = runTest {
        val userMessage = UserMessage(id = 1, message = "Example user message")
        loginViewModel.onEvent(LoginUiEvent.DismissUserMessage(userMessage.id ?: return@runTest))
        val userMessagesState = loginViewModel.state.value.userMessages
        assertThat(userMessagesState).doesNotContain(userMessage)
    }

    @Test
    fun `login with valid details results to true`() = runTest {
        val user = User(
            username = "Jack", password = "ab29m%*"
        )
        /*
        * wait for coroutines to finish before calling assert
        * */
        loginViewModel.onEvent(LoginUiEvent.OnLogin(user))
        advanceUntilIdle()
        val loginSuccess = loginViewModel.state.value.loginSuccess
        assertThat(loginSuccess).isTrue()
    }

    @Test
    fun `login with invalid username or password results to false`() = runTest {
        val user = User(
            username = "Asma", password = "34rt%*"
        )
        loginViewModel.onEvent(LoginUiEvent.OnLogin(user))

        /*
        * wait for coroutines to finish before calling assert
        * */
        advanceUntilIdle()
        val loginSuccess = loginViewModel.state.value.loginSuccess
        assertThat(loginSuccess).isFalse()
    }

    @Test
    fun `initial login status returns false`() = runTest {
        val loginStatus = loginViewModel.state.value.loginSuccess
        assertThat(loginStatus).isFalse()
    }
}
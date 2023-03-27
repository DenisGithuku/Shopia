package com.githukudenis.auth.ui

import androidx.compose.runtime.Immutable
import com.githukudenis.core_data.util.UserMessage

@Immutable
data class LoginUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val formState: FormState = FormState(),
    val userMessages: List<UserMessage> = emptyList()
)


val LoginUiState.formIsValid get() = !formState.username.isNullOrEmpty() && !formState.password.isNullOrEmpty()

@Immutable
data class FormState(
    val username: String = "",
    val password: String = "",
    val passwordIsVisible: Boolean = false
)

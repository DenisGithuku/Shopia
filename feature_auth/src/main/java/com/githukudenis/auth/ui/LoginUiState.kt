package com.githukudenis.auth.ui

import androidx.compose.runtime.Immutable

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

@Immutable
data class UserMessage(
    val id: Int? = null,
    val message: String? = null
)
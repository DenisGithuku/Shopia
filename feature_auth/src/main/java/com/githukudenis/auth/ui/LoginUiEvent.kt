package com.githukudenis.auth.ui

import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.auth.api.User

sealed class LoginUiEvent {
    class OnShowUserMessage(val userMessage: UserMessage): LoginUiEvent()
    class DismissUserMessage(val messageId: Int): LoginUiEvent()
    class OnLogin(val user: User): LoginUiEvent()
    class OnUsernameChange(val username: String): LoginUiEvent()
    class OnPasswordChange(val password: String): LoginUiEvent()
    object OnTogglePasswordVisibility: LoginUiEvent()
}
package com.githukudenis.auth.ui

import com.githukudenis.auth.api.User

sealed class LoginUiEvent {
    class OnShowUserMessage(val userMessage: UserMessage): LoginUiEvent()
    class OnUserMessageShown(val messageId: Int): LoginUiEvent()
    class OnLogin(val user: User): LoginUiEvent()
    class OnUsernameChange(val username: String): LoginUiEvent()
    class OnPasswordChange(val password: String): LoginUiEvent()
    object OnTogglePasswordVisibility: LoginUiEvent()
}
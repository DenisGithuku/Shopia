package com.githukudenis.feature_user.ui.profile

sealed class ProfileUiEvent {
    object Logout: ProfileUiEvent()

    class DismissUserMessage(val messageId: Int): ProfileUiEvent()
}

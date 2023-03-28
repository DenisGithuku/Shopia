package com.githukudenis.feature_user.ui.profile

import com.githukudenis.core_data.util.UserMessage
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem

data class ProfileUiState(
    val isLoading: Boolean = false,
    val signedOut: Boolean = false,
    val profile: UsersDTOItem? = null,
    val userMessages: List<UserMessage> = emptyList()
)
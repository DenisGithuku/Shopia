package com.githukudenis.feature_user.ui.profile

import com.githukudenis.feature_user.data.remote.model.UsersDTOItem

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UsersDTOItem? = null
)
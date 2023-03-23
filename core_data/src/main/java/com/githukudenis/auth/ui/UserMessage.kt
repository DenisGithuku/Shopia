package com.githukudenis.auth.ui

import androidx.compose.runtime.Immutable

@Immutable
data class UserMessage(
    val id: Int? = null,
    val message: String? = null
)
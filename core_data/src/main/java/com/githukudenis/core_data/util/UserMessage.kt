package com.githukudenis.core_data.util

import androidx.compose.runtime.Immutable

@Immutable
data class UserMessage(
    val id: Int? = null,
    val message: String? = null
)
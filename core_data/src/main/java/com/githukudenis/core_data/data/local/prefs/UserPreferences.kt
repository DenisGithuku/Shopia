package com.githukudenis.core_data.data.local.prefs

data class UserPreferences(
    val appInitialized: Boolean = false,
    val userLoggedIn: Boolean = false,
    val userId: Int? = null,
    val username: String? = null
)
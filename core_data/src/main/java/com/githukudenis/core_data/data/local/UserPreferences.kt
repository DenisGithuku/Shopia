package com.githukudenis.core_data.data.local

data class UserPreferences(
    val appInitialized: Boolean = false,
    val userLoggedIn: Boolean = false
)
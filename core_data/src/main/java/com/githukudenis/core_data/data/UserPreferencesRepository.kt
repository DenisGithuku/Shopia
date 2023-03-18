package com.githukudenis.core_data.data

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateAppInitialization(appInitialized: Boolean)

    suspend fun updateUserLoggedIn(loggedIn: Boolean)
}
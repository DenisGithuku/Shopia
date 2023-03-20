package com.githukudenis.auth.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserPreferencesRepositoryImpl: UserPreferencesRepository {

    private val userPrefs = UserPreferences(appInitialized = false, userLoggedIn = false)

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = flow { userPrefs }

    override suspend fun updateAppInitialization(appInitialized: Boolean) {
        userPrefs.copy(
            appInitialized = appInitialized
        )
    }

    override suspend fun updateUserLoggedIn(loggedIn: Boolean) {
        userPrefs.copy(
            userLoggedIn = loggedIn
        )
    }
}
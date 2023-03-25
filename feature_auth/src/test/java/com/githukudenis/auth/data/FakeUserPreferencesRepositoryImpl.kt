package com.githukudenis.auth.data

import com.githukudenis.core_data.data.local.prefs.UserPreferences
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
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

    override suspend fun storeUserId(userId: Int) {
        userPrefs.copy(
            userId = userId
        )
    }

    override suspend fun storeUserName(username: String) {
        userPrefs.copy(
            username = username
        )
    }
}
package com.githukudenis.feature_user.ui.profile

import com.githukudenis.core_data.data.local.prefs.UserPreferences
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPrefsRepository: UserPreferencesRepository {
    val userPreferences = UserPreferences()

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = flowOf(userPreferences)

    override suspend fun updateAppInitialization(appInitialized: Boolean) {
        userPreferences.copy(
            appInitialized = appInitialized
        )
    }

    override suspend fun updateUserLoggedIn(loggedIn: Boolean) {
        userPreferences.copy(
            userLoggedIn = loggedIn
        )
    }

    override suspend fun storeUserId(userId: Int) {
        userPreferences.copy(
            userId = userId
        )
    }

    override suspend fun storeUserName(username: String) {
        userPreferences.copy(
            username = username
        )
    }
}
package com.githukudenis.feature_product.ui.views.products

import com.githukudenis.core_data.data.local.prefs.UserPreferences
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPrefsRepository: UserPreferencesRepository {
    var userPreferences = UserPreferences()

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = flowOf(userPreferences)

    override suspend fun updateAppInitialization(appInitialized: Boolean) {
        userPreferences = userPreferences.copy(
            appInitialized = appInitialized
        )
    }

    override suspend fun updateUserLoggedIn(loggedIn: Boolean) {
        userPreferences = userPreferences.copy(
            userLoggedIn = loggedIn
        )
    }

    override suspend fun storeUserId(userId: Int?) {
        userPreferences = userPreferences.copy(
            userId = userId
        )
    }

    override suspend fun storeUserName(username: String) {
        userPreferences = userPreferences.copy(
            username = username
        )
    }

    override suspend fun resetPreferences() {
        userPreferences = userPreferences.copy(
            appInitialized = false,
            userLoggedIn = false,
            userId = null,
            username = null
        )
    }
    override suspend fun updateFavourites(favourites: Set<Int>) {
        userPreferences = userPreferences.copy(
            favourites = favourites
        )
    }
}
package com.githukudenis.core_data.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.githukudenis.core_data.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val context: Context
) : UserPreferencesRepository {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.userPreferences)

    override val userPreferencesFlow: Flow<UserPreferences>
        get() = context.datastore.data.map { prefs ->
            val appInitialized = prefs[PreferenceKeys.APP_INITIALIZED] ?: false
            val userLoggedIn = prefs[PreferenceKeys.USER_LOGGED_IN] ?: false
            val userId = prefs[PreferenceKeys.USER_ID]
            val username = prefs[PreferenceKeys.USER_NAME]
            UserPreferences(appInitialized, userLoggedIn, userId, username)
        }

    override suspend fun storeUserId(userId: Int?) {
        context.datastore.edit { prefs ->
            userId?.let { id ->
                prefs[PreferenceKeys.USER_ID] = id
            }

        }
    }

    override suspend fun updateAppInitialization(appInitialized: Boolean) {
        context.datastore.edit { prefs ->
            prefs[PreferenceKeys.APP_INITIALIZED] = appInitialized
        }
    }

    override suspend fun updateUserLoggedIn(loggedIn: Boolean) {
        context.datastore.edit { prefs ->
            prefs[PreferenceKeys.USER_LOGGED_IN] = loggedIn
        }
    }

    override suspend fun storeUserName(username: String) {
        context.datastore.edit { prefs ->
            prefs[PreferenceKeys.USER_NAME] = username
        }
    }

    override suspend fun resetPreferences() {
        context.datastore.edit {prefs ->
            prefs[PreferenceKeys.USER_ID] = -1
            prefs[PreferenceKeys.USER_LOGGED_IN] = false
            prefs[PreferenceKeys.USER_NAME] = ""
            prefs[PreferenceKeys.APP_INITIALIZED] = false
        }
    }
}

private object PreferenceKeys {
    val APP_INITIALIZED = booleanPreferencesKey("app_initialized")
    val USER_LOGGED_IN = booleanPreferencesKey("user_logged_in")
    val USER_ID = intPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("username")
}
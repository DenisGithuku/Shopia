package com.githukudenis.core_data.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.githukudenis.core_data.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.userPreferences)

    override suspend fun getAppStartStatus(scope: CoroutineScope): Boolean {
        val appStatusPref = booleanPreferencesKey(Constants.appStatusKey)
        return context.datastore.data.first()[appStatusPref] ?: false
    }

    override suspend fun updateAppStatusPreference(value: Boolean) {
        val appStatusPref = booleanPreferencesKey(Constants.appStatusKey)
        context.datastore.edit { prefs ->
            prefs[appStatusPref] = value
        }
    }
}
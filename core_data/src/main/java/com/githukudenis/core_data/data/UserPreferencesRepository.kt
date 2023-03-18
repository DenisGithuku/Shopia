package com.githukudenis.core_data.data

import kotlinx.coroutines.CoroutineScope

interface UserPreferencesRepository {

    suspend fun getAppStartStatus(scope: CoroutineScope): Boolean

    suspend fun updateAppStatusPreference(value: Boolean)
}
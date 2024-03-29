package com.githukudenis.auth.data

import com.githukudenis.auth.api.LoginApiService
import com.githukudenis.auth.api.User
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
) : AuthRepository {
    override suspend fun login(user: User): String? {
        return try {
            withContext(shopiaCoroutineDispatcher.ioDispatcher) {
                val response = loginApiService.login(user)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    userPreferencesRepository.updateUserLoggedIn(true)
                    token
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
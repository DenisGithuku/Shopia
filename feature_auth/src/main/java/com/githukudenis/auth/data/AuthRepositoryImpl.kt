package com.githukudenis.auth.data

import com.githukudenis.auth.api.LoginApiService
import com.githukudenis.auth.api.User
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService
) : AuthRepository {
    override suspend fun login(user: User): String? {
        return try {
            val response = loginApiService.login(user)
            if (response.isSuccessful) {
                val token = response.body()?.token
                token
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
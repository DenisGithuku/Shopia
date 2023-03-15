package com.githukudenis.auth.data

import com.githukudenis.auth.api.User

interface AuthRepository {
    suspend fun login(user: User): String?
}
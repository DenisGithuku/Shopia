package com.githukudenis.data

import com.githukudenis.auth.login.User

interface AuthRepository {
    suspend fun login(user: User): String?
}
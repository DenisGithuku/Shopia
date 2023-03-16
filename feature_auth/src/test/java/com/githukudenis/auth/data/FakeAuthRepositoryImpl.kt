package com.githukudenis.auth.data

import com.githukudenis.auth.api.User
import timber.log.Timber

class FakeAuthRepositoryImpl : AuthRepository {
    private val usersDb = listOf(
        User(
            username = "Alan", password = "34rtW&_"
        ),
        User(
            username = "Sam", password = "2r6754_"
        ),
        User(
            username = "Jack", password = "ab29m%*"
        ),
    )

    override suspend fun login(user: User): String? {
        return try {
            val userInDb = usersDb.any { userInDb -> userInDb == user }
            if (userInDb) "Some token" else null
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}
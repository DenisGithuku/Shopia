package com.githukudenis.feature_user.data

import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.feature_user.data.remote.UserApiService
import com.githukudenis.feature_user.data.remote.model.UsersDTO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersApiService: UserApiService,
    private val shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
) : UserRepository {
    override val users: Flow<UsersDTO?>
        get() = flow {
            try {
                val response = usersApiService.getAllUsers()
                if (response.isSuccessful) {
                    val users = response.body()
                    emit(users)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)

    override suspend fun getUserByUserName(username: String): Flow<UsersDTOItem?> {
        return flow {
            try {
                val response = usersApiService.getAllUsers()
                if (response.isSuccessful) {
                    val allUsers = response.body()
                    allUsers?.let { usersDTO ->
                        emit(usersDTO.find { user -> user.username == username })
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }.flowOn(shopiaCoroutineDispatcher.ioDispatcher)
    }
}
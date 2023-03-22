package com.githukudenis.feature_user.data

import com.githukudenis.feature_user.data.remote.model.UsersDTO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val users: Flow<UsersDTO?>

    suspend fun getUserByUserName(username: String): Flow<UsersDTOItem?>
}
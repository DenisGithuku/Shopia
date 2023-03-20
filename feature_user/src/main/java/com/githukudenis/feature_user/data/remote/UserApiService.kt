package com.githukudenis.feature_user.data.remote

import com.githukudenis.feature_user.data.remote.model.UsersDTO
import com.githukudenis.feature_user.data.remote.model.UsersDTOItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("users")
    suspend fun getAllUsers(): Response<UsersDTO>

    @GET("users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int
    ): Response<UsersDTOItem>
}
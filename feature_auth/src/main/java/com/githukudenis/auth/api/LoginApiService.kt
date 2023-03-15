package com.githukudenis.auth.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface LoginApiService {
    @POST("auth/login")
    suspend fun login(
        @Body user: User
    ): Response<String>
}
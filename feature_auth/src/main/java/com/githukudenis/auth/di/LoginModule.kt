package com.githukudenis.auth.di

import com.githukudenis.auth.api.LoginApiService
import com.githukudenis.auth.data.AuthRepository
import com.githukudenis.auth.data.AuthRepositoryImpl
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val baseUrl = "https://fakestoreapi.com/"

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideUserLoginService(okHttpClient: OkHttpClient): LoginApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(loginApiService: LoginApiService, userPreferencesRepository: UserPreferencesRepository): AuthRepository {
        return AuthRepositoryImpl(loginApiService, userPreferencesRepository)
    }
}
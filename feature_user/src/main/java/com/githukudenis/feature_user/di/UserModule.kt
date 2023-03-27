package com.githukudenis.feature_user.di

import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.core_data.util.Constants
import com.githukudenis.feature_user.data.UserRepository
import com.githukudenis.feature_user.data.UsersRepositoryImpl
import com.githukudenis.feature_user.data.remote.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApiService(okHttpClient: OkHttpClient): UserApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApiService: UserApiService, shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher): UserRepository {
        return UsersRepositoryImpl(userApiService, shopiaCoroutineDispatcher)
    }
}
package com.githukudenis.feature_cart.di

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.util.Constants
import com.githukudenis.feature_cart.data.remote.CartApiService
import com.githukudenis.feature_cart.data.repo.CartRepository
import com.githukudenis.feature_cart.data.repo.CartRepositoryImpl
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
object CartModule {

    @Provides
    @Singleton
    fun provideCartApiService(okHttpClient: OkHttpClient): CartApiService {
        return Retrofit.Builder().baseUrl(Constants.baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CartApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartApiService: CartApiService, cartDao: CartDao
    ): CartRepository {
        return CartRepositoryImpl(cartApiService, cartDao = cartDao)
    }
}
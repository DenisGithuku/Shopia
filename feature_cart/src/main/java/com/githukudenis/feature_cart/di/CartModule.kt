package com.githukudenis.feature_cart.di

import com.githukudenis.core_data.data.local.db.CartDao
import com.githukudenis.core_data.data.local.prefs.UserPreferencesRepository
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
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
        cartApiService: CartApiService,
        cartDao: CartDao,
        shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher,
        productsRepository: ProductsRepository,
        userPreferencesRepository: UserPreferencesRepository
    ): CartRepository {
        return CartRepositoryImpl(
            cartApiService = cartApiService,
            cartDao = cartDao,
            productsRepository = productsRepository,
            shopiaCoroutineDispatcher = shopiaCoroutineDispatcher,
            userPreferencesRepository = userPreferencesRepository
        )
    }
}
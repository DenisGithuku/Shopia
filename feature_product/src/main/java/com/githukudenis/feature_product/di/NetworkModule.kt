package com.githukudenis.feature_product.di

import android.content.Context
import com.githukudenis.core_data.data.local.db.model.ProductsDao
import com.githukudenis.core_data.data.repository.ProductsRepository
import com.githukudenis.core_data.data.repository.ProductsRepositoryImpl
import com.githukudenis.core_data.di.ShopiaCoroutineDispatcher
import com.githukudenis.feature_product.data.ProductsApiService
import com.githukudenis.feature_product.data.base_url
import com.githukudenis.feature_product.data.util.NetworkObserver
import com.githukudenis.feature_product.data.util.NetworkStateObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideProductsApi(okHttpClient: OkHttpClient): ProductsApiService {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ProductsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(
        productsDao: ProductsDao,
        productsApiService: ProductsApiService,
        shopiaCoroutineDispatcher: ShopiaCoroutineDispatcher
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            productsDao = productsDao,
            productsApiService = productsApiService,
            shopiaCoroutineDispatcher = shopiaCoroutineDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideNetworkStateObserver(
        @ApplicationContext
        context: Context
    ): NetworkObserver {
        return NetworkStateObserver(context = context)
    }
}
package com.githukudenis.coroutinesindustrialbuild.di

import android.content.Context
import com.githukudenis.coroutinesindustrialbuild.data.api.ProductsApiService
import com.githukudenis.coroutinesindustrialbuild.data.api.base_url
import com.githukudenis.coroutinesindustrialbuild.data.repo.ProductsDatasource
import com.githukudenis.coroutinesindustrialbuild.data.repo.ProductsRepo
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkObserver
import com.githukudenis.coroutinesindustrialbuild.data.util.NetworkStateObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
//            .callTimeout(timeout = 10000L, TimeUnit.MILLISECONDS)
            .build()
    }

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
        productsApiService: ProductsApiService
    ): ProductsRepo {
        return ProductsDatasource(productsApiService = productsApiService)
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